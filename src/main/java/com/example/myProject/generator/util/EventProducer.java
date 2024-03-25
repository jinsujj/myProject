package com.example.myproject.generator.util;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;


public class EventProducer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    private Producer<String, String> producer;
    private String transactionIdPrefix = "trans-"; 
    private final String financialEventsTopic = "FinancialEvents";
    private Properties props = new Properties();

    
    public EventProducer(Producer<String, String> producer) {
        this.producer = producer;
        producer.initTransactions();
    }

    public EventProducer(){
        // Producer 설정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // EOS(Exactly Once Semantics) 설정
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,transactionIdPrefix + Uuid.randomUuid().toString());
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        
        this.producer = new KafkaProducer<>(props);
        producer.initTransactions();
    }

    // key는 고객 ID, value는 로그 내역
    public void send(String key, String value) {
        try {
            producer.beginTransaction();
            producer.send(new ProducerRecord<>(financialEventsTopic, key, value));
            producer.commitTransaction();
        } catch (Exception e) {
            System.out.println("Failed to send message: " + e.getMessage());
            producer.abortTransaction();
        }
    }

    public void close(){
        producer.close();
    }
}
