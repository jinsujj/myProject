package com.example.myproject.profiler.processor;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.response.log.AccountOpeningLog;
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

        Optional<Customer> customerByNumber = bank.findCustomerByNumber(customerNumber);

        if(customerByNumber.isPresent()){
            Customer customer = customerByNumber.get();
            customer.addSession();
            customer.openAccount(accountNumber);
            System.out.println(openAccountLog.toJson());
        }
        else{
            System.out.println("'Open Acount' Customer not found  : " + customerNumber);
        }
    }
}
