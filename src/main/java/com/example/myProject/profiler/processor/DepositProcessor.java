package com.example.myproject.profiler.processor;


import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.response.log.DepositLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DepositProcessor implements MessageProcessor{
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;
    private long depositAmount;
    private String depositTime;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        DepositLog depositLog = mapper.readValue(record.value(), DepositLog.class);

        customerNumber = depositLog.getCustomerNumber();
        depositAmount = depositLog.getDepositAmount();
        depositTime = depositLog.getDepositTime();

        Optional<Customer> customerOptional = bank.findCustomerByNumber(customerNumber);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.addSession();
            customer.deposit(depositAmount, depositTime);
            System.out.println(depositLog.toJson());
        } else {
             System.out.println("'Deposit' Customer not found  : " + customerNumber);
        }
    }
}
