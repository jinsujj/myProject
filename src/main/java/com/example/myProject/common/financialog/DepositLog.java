package com.example.myproject.common.financialog;

import java.time.LocalDateTime;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class DepositLog extends BaseLog {
    private String customerNumber;
    private String depositAccountNumber;
    private long depositAmount;
    private String depositTime;
    private String action;

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
