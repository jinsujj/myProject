package com.example.myproject.profiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseProcessor {
    protected ObjectMapper mapper = new ObjectMapper();

    public abstract void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException;
}
