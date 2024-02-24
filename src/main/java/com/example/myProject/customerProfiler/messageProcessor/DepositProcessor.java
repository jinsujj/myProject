package com.example.myProject.customerProfiler.messageProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.financialLog.v1.DepositLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DepositProcessor implements MessageProcessor{
    
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(ConsumerRecord<String, String> record) throws JsonProcessingException {
        DepositLog depositLog = mapper.readValue(record.value(), DepositLog.class);
        System.out.println("Processing deposit log: " + depositLog.getCustomerNumber() +" "+ depositLog.getDepositAccountNumber());
    }
    
}
