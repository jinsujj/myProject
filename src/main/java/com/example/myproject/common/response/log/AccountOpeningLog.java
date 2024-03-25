package com.example.myproject.common.response.log;

import java.time.LocalDateTime;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AccountOpeningLog extends BaseLog {
    private final String customerNumber;
    private final String accountNumber;
    private final String accountOpeningTime;
    private final String action;

    public AccountOpeningLog(){
        this.customerNumber = "";
        this.accountNumber = "";
        this.accountOpeningTime = "";
        this.action = "";

    }
    
    public AccountOpeningLog(String customerNumber, String accountNumber, LocalDateTime accountOpeningTime) {
        this.customerNumber = customerNumber;
        this.accountNumber = accountNumber;
        this.accountOpeningTime = accountOpeningTime.format(formatter);
        this.action = FinancialAction.OPEN_ACCOUNT.name();
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

    public String getAction() {
        return action;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
