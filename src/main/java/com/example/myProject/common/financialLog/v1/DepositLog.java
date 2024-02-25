package com.example.myProject.common.financialLog.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.myProject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DepositLog {
    private String customerNumber;
    private String depositAccountNumber;
    private long depositAmount;
    private String depositTime;
    private String action;

    private transient ObjectMapper mapper = new ObjectMapper();
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DepositLog() {
    }

    public DepositLog(String customerNumber, String depositAccountNumber, long depositAmount, LocalDateTime depositTime) {
        this.customerNumber = customerNumber;
        this.depositAccountNumber = depositAccountNumber;
        this.depositAmount = depositAmount;
        this.depositTime = depositTime.format(formatter);
        this.action = FinancialAction.DEPOSIT.name();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getDepositAccountNumber() {
        return depositAccountNumber;
    }

    public long getDepositAmount() {
        return depositAmount;
    }

    public String getDepositTime() {
        return depositTime;
    }

    public String getAction() {
        return action;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}   
