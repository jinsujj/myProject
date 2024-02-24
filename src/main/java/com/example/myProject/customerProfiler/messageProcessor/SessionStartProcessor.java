package com.example.myProject.customerProfiler.messageProcessor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.financialLog.v1.SessionStartLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionStartProcessor implements MessageProcessor {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void process(ConsumerRecord<String, String> record) throws JsonProcessingException {
        SessionStartLog sessionStartLog = mapper.readValue(record.value(), SessionStartLog.class);
        if (!sessionStartLog.getCustomerNumber().isEmpty()){
            String customerNumber = sessionStartLog.getCustomerNumber();
            // 여기에 추가 로직을 구현합니다.
        }
    }
}
