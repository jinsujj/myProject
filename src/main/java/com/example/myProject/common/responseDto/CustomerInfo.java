package com.example.myProject.common.responseDto;

import com.example.myProject.common.domain.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerInfo {
    private String customerNumber;
    private String name;
    private String birthDate;
    private String joinDateTime;
    private long sessionCount;

    private transient ObjectMapper mapper = new ObjectMapper();

    public CustomerInfo(Customer customer ) {
        this.customerNumber = customer.getCustomerNumber();
        this.name = customer.getName();
        this.birthDate = customer.getDateOfBirth();
        this.joinDateTime = customer.getJoinDateTime();
        this.sessionCount = customer.getSessionCount();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getJoinDateTime() {
        return joinDateTime;
    }

    public long getSessionCount() {
        return sessionCount;
    }


    public String toJson() throws JsonProcessingException{
        return mapper.writeValueAsString(this);
    }   
}
