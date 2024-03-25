package com.example.myproject.customerprofiler;

import java.util.Properties;
import java.util.Arrays;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ConsumerRunner implements Runnable{
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    private static final String ISOLATION_LEVEL = "read_committed";

    private static final int MAX_RETRY = 3; // 최대 재시도 횟수
    private static final long RETRY_INTERVAL_MS = 1000; // 재시도 간격 (밀리초)
    private static final int CONSUME_INTERVAL_MS = 100; // 컨슈머 폴링 주기 (밀리초)

    private KafkaConsumer<String, String> consumer;
    private final String topic;
    private final String groupId;
    private Properties props;
    private Bank bank;

    public ConsumerRunner(String topic, String groupId, Properties props, Bank bank) {
        this.topic = topic;
        this.groupId = groupId;
        this.props = props;
        this.bank = bank;
    }
    // for test code
    protected void setConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void run() {
        // Consumer 설정
        this.props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        this.props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // EOS(Exactly Once Semantics) 설정
        this.props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, ISOLATION_LEVEL);
        this.props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");   
        this.props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(CONSUME_INTERVAL_MS));
                for (ConsumerRecord<String, String> record : records) {
                    
                    String actionValue = new ObjectMapper().readTree(record.value()).get("action").asText();
                    FinancialAction action = FinancialAction.valueOf(actionValue.toUpperCase());

                    MessageProcessor processor = new ProcessorFactory().getProcessor(action);
                    if (processor != null) {
                        processor.process(record,bank);
                    }
                }
                asyncCommit(consumer, MAX_RETRY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

    
    // 비동기 커밋 및 재시도 로직
    protected void asyncCommit(KafkaConsumer<String, String> consumer, int remainingRetries) {
        consumer.commitAsync((offsets, exception) -> {
            if (exception != null) {
                if (remainingRetries > 0) {
                    retryCommit(consumer, remainingRetries);
                } else {
                    handleCommitFailure(offsets, exception);
                }
            }
        });
    }

    // 커밋 재시도 로직
    protected void retryCommit(KafkaConsumer<String, String> consumer, int remainingRetries) {
        System.out.println(consumer + "Retrying commit...: " + remainingRetries);
        try {
            Thread.sleep(RETRY_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        remainingRetries--;
        asyncCommit(consumer, remainingRetries);
    }

    // 실패한 offset 저장 로직
    public void handleCommitFailure(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        System.out.println("Commit failed after retries: " + exception.getMessage());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed_consumer_offset.txt", true))) {
            for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
                writer.write("Topic: " + entry.getKey().topic() + 
                            " Partition: " + entry.getKey().partition() +
                            " Offset: " + entry.getValue().offset() + "\n"
                            );
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
