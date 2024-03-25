package com.example.myproject.profiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface BaseProcessor {
    void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException;
}