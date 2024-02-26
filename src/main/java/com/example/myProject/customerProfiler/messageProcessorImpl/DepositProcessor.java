package com.example.myProject.customerProfiler.messageProcessorImpl;


import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.financialLog.v1.DepositLog;
import com.example.myProject.customerProfiler.MessageProcessor;
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
