package com.example.myproject.common.response.log;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;



public class SignUpLog extends BaseLog {
    private String customerNumber;
    private String customerName;
    private String dateOfBirth;
    private String registrationTime;
    private String action;

    public SignUpLog() {
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
