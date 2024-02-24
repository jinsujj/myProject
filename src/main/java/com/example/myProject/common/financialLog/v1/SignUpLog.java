package com.example.myProject.common.financialLog.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class SignUpLog {
    private String customerNumber;
    private String customerName;
    private String dateOfBirth;
    private String registrationTime;

    private transient ObjectMapper mapper = new ObjectMapper();

    public SignUpLog() {
    }

    public SignUpLog(String customerNumber, String customerName, String dateOfBirth, String registrationTime) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.dateOfBirth = dateOfBirth;
        this.registrationTime = registrationTime;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
