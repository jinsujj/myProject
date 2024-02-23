package com.example.myProject.CustomerProfiler.util;

import java.util.Properties;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

/*
 * Runnable 인터페이스를 사용하는 이유
 * Java에서 멀티스레딩을 구현하는데 있어서 가장 기본적이고 중요한 방법 중 하나
 * 'Runnable'은 단 하나의 메소드인 'run()' 메소드만을 가지고 있는 인터페이스
 *  이 메소드에 스레드가 실행할 코드를 정의합니다.
 * 
 * 1. 스레드에 작업을 제공
 * Java 에서 스레드를 생성하고 실행할 때, Runnable 인터페이스를 구현하는 클래스의 인스턴스를
 * 'Thread' 생성자에 전달할 수 있습니다.
 * 이렇게 하면 Thread 객체가 생성될 때, Runnable 객체에 정의된 run() 메소드에
 * 작성된 작업을 쓰레드가 실행하게 됩니다.
 * 
 * 2. 작업 코드와 스레드 코드의 분리
 * Runnable 인터페이스를 사용하면, 실행할 작업과 스레드를 관리하는 코드를 분리할 수 있습니다.
 * 이는 코드의 재사용성과 유지보수성을 향상시킵니다.
 * 
 * 3. 자바의 단일 상속 제한 우회    
 * Java는 단일 상속만을 지원하기 때문에, Thread 클래스를 직접 상속받으면 다른 클래스를 상속받을 수 없습니다.
 * Runnable 인터페이스를 구현함으로써, 다른 클래스를 상속받으면서 동시에 쓰레드 작업을 정의할 수 있습니다.
 * 
 * 4. 스레드 풀과의 호환성
 * Java 의 동시성 API 는 ExecutorService 라는 스레드 풀을 제공하여, 스레드의 생성과 관리를 추상화하고 최적화합니다.
 * ExecutorService 를 사용할 때, Runnable 또는 Callable 인터페이스를 구현하는 클래스의 인스턴스를 제출할 수 있습니다.
 * Runnable 을 사용하면 이러한 고수준 동시성 몌ㅑ의 호환성이 보장됩니다.
 */

public class ConsumerRunner implements Runnable{
    private static final int MAX_RETRY = 3; // 최대 재시도 횟수
    private static final long RETRY_INTERVAL_MS = 1000; // 재시도 간격 (밀리초)

    private final String topic;
    private final String groupId;
    private Properties props;

    public ConsumerRunner(String topic, String groupId, Properties props) {
        this.topic = topic;
        this.groupId = groupId;
        this.props = props;
    }

    @Override
    public void run() {
        // 토픽별 컨슈머 그룹 생성
        this.props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    processRecord(record);
                }
                asyncCommitWithRetry(consumer, MAX_RETRY);
            }
        } finally {
            consumer.close();
        }
    }

    // 메시지 처리 로직
    private void processRecord(ConsumerRecord<String, String> record) {
        System.out.printf("Processed record with key %s and value %s%n", record.key(), record.value());
    }

    
    // 비동기 커밋 및 재시도 로직
    private static void asyncCommitWithRetry(KafkaConsumer<String, String> consumer, int remainingRetries) {
        consumer.commitAsync((offsets, exception) -> {
            if (exception != null) {
                System.out.println("Commit failed, retrying...: " + remainingRetries);
                if (remainingRetries > 0) {
                    retryCommit(consumer, remainingRetries);
                } else {
                    handleCommitFailure(offsets, exception);
                }
            }
        });
    }

    // 커밋 재시도 로직
    private static void retryCommit(KafkaConsumer<String, String> consumer, int remainingRetries) {
        try {
            Thread.sleep(RETRY_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        asyncCommitWithRetry(consumer, remainingRetries - 1);
    }

    // 실패한 offset 저장 로직
    private static void handleCommitFailure(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
        System.out.println("Commit failed after retries: " + exception.getMessage());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("failed_consumer_offset.txt", true))) {
            for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
                writer.write("Topic: " + entry.getKey().topic() + " Partition: " + entry.getKey().partition() +
                             " Offset: " + entry.getValue().offset() + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
