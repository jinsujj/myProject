package com.example.myProject.common.financialLog.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class SignUpLog {
    private String customerNumber;
    private String customerName;
    private String dateOfBirth;
    private String registrationTime;

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SignUpLog(String customerNumber, String customerName, String dateOfBirth, LocalDateTime registrationTime) {
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.dateOfBirth = dateOfBirth;
        this.registrationTime = registrationTime.format(formatter);
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
}
