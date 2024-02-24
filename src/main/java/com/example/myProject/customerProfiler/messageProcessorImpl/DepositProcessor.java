package com.example.myProject.customerProfiler.messageProcessorImpl;


import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.financialLog.v1.DepositLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DepositProcessor implements MessageProcessor{
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;
    private long depositAmount;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        DepositLog depositLog = mapper.readValue(record.value(), DepositLog.class);

        customerNumber = depositLog.getCustomerNumber();
        depositAmount = depositLog.getDepositAmount();

        bank.findCustomerByNumber(customerNumber).ifPresent(customer -> {
            customer.addSession();
            customer.deposit(depositAmount);
        });

        System.out.println("Deposit:" +depositLog.toJson());
    }
    
}
