package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.financialLog.v1.AccountOpeningLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAccountProcessor implements MessageProcessor {
    private ObjectMapper mapper = new ObjectMapper();
    
    private String accountNumber;
    private String customerNumber;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        AccountOpeningLog openAccountLog = mapper.readValue(record.value(), AccountOpeningLog.class);

        accountNumber = openAccountLog.getAccountNumber();
        customerNumber = openAccountLog.getCustomerNumber();

        bank.findCustomerByNumber(customerNumber).ifPresent(customer -> {
            customer.addSession();
            customer.openAccount(accountNumber);
        });

        System.out.println("OpenAccount: "+ openAccountLog.toJson());
        
    }

    

}
