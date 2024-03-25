package com.example.myproject.customerprofiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.financialog.SignUpLog;
import com.example.myproject.customerprofiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SignUpProcessor implements MessageProcessor{
    private ObjectMapper mapper = new ObjectMapper();
    private String customerName;
    private String customerNumber;
    private String dateOfBirth;
    private String registrationTime;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        SignUpLog signUpLog = mapper.readValue(record.value(), SignUpLog.class);

        customerName = signUpLog.getCustomerName();
        customerNumber = signUpLog.getCustomerNumber();
        dateOfBirth = signUpLog.getDateOfBirth();
        registrationTime = signUpLog.getRegistrationTime();

        Customer customer = new Customer(customerNumber,customerName, dateOfBirth, registrationTime);
        bank.signupCustomer(customer);

        System.out.println(signUpLog.toJson());
    }

    
    
}