package com.example.myproject.common.response.log;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;



public class SignUpLog extends BaseLog {
    private final String customerNumber;
    private final String customerName;
    private final String dateOfBirth;
    private final String registrationTime;
    private final String action;

    public SignUpLog(){
        this.customerNumber = "";
        this.customerName = "";
        this.dateOfBirth = "";
        this.registrationTime = "";
        this.action = FinancialAction.SIGNUP.name();
    }

    public SignUpLog(String customerNumber, String customerName, String dateOfBirth, String registrationTime) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.dateOfBirth = dateOfBirth;
        this.registrationTime = registrationTime;
        this.action = FinancialAction.SIGNUP.name();
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

    public String getAction() {
        return action;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
