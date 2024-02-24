package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.financialLog.v1.SignUpLog;
import com.example.myProject.customerProfiler.MessageProcessor;
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

        System.out.println("SignUp: "+signUpLog.toJson());
    }

    
    
}
