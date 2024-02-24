package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.financialLog.v1.SessionStartLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionStartProcessor implements MessageProcessor {
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        SessionStartLog sessionStartLog = mapper.readValue(record.value(), SessionStartLog.class);

        customerNumber = sessionStartLog.getCustomerNumber();

        bank.findCustomerByNumber(customerNumber).ifPresent(customer -> {
            customer.addSession();
        });

        System.out.println("SessionStart: "+sessionStartLog.toJson());
    }
}
