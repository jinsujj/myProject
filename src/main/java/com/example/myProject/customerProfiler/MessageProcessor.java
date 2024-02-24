package com.example.myProject.customerProfiler;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageProcessor {
    void process(ConsumerRecord<String, String> record) throws JsonProcessingException;
}
