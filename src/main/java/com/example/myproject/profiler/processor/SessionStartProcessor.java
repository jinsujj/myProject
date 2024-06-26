package com.example.myproject.profiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.response.log.SessionStartLog;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SessionStartProcessor extends BaseProcessor {
    private String customerNumber;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        SessionStartLog sessionStartLog = this.mapper.readValue(record.value(), SessionStartLog.class);

        customerNumber = sessionStartLog.getCustomerNumber();
        if (customerNumber.isEmpty())
            return;
            
        bank.findCustomerByNumber(customerNumber).ifPresent(customer -> {
            customer.addSession();
        });
        System.out.println(sessionStartLog.toJson());
    }
}
