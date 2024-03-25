package com.example.myproject.common.response.dto;

import com.example.myproject.common.domain.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CustomerInfo extends BaseInfo {
    private final String customerNumber;
    private final String name;
    private final String birthDate;
    private final String joinDateTime;
    private final long sessionCount;

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
