package com.example.myProject.common.financialLog.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountOpeningLog {
    private String customerNumber;
    private String accountNumber;
    private String accountOpeningTime;

    private transient ObjectMapper mapper = new ObjectMapper();
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AccountOpeningLog() {
    }

    public AccountOpeningLog(String customerNumber, String accountNumber, LocalDateTime accountOpeningTime) {
        this.customerNumber = customerNumber;
        this.accountNumber = accountNumber;
        this.accountOpeningTime = accountOpeningTime.format(formatter);
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountOpeningTime() {
        return accountOpeningTime;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
