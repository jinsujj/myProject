package com.example.myProject.customerProfiler.messageProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.financialLog.v1.TransferLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransferProcessor implements MessageProcessor{
    
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(ConsumerRecord<String, String> record) throws JsonProcessingException {
        TransferLog transferLog = mapper.readValue(record.value(), TransferLog.class);
    }
}
