package com.example.myProject;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;

public class KafkaProducerConfig {
    private Producer<String, String> producer;
    private String transactionIdPrefix = "trans-"; 

    public KafkaProducerConfig(){
        Properties props = new Properties();
        // Broker 서버 리스트 설정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // EOS(Exactly Once Semantics) 설정
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG,transactionIdPrefix + Uuid.randomUuid().toString());
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.RETRIES_CONFIG, 5);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        
        this.producer = new KafkaProducer<>(props);
        producer.initTransactions();
    }

    public void send(String topic, String key, String value){
        try{
            producer.beginTransaction();
            producer.send(new ProducerRecord<>(topic, key, value));
            producer.commitTransaction();
        }
        catch (Exception e){
            producer.abortTransaction();
        }
    }

    public void close(){
        producer.close();
    }
}
