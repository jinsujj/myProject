package com.example.myproject.profiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.response.log.SignUpLog;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SignUpProcessor extends BaseProcessor{
    private String customerName;
    private String customerNumber;
    private String dateOfBirth;
    private String registrationTime;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        SignUpLog signUpLog = this.mapper.readValue(record.value(), SignUpLog.class);

        customerName = signUpLog.getCustomerName();
        customerNumber = signUpLog.getCustomerNumber();
        dateOfBirth = signUpLog.getDateOfBirth();
        registrationTime = signUpLog.getRegistrationTime();

        Customer customer = new Customer(customerNumber,customerName, dateOfBirth, registrationTime);
        bank.signupCustomer(customer);

        System.out.println(signUpLog.toJson());
    }
}
