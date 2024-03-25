package com.example.myproject.common.financialog;

import java.time.LocalDateTime;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AccountOpeningLog extends BaseLog {
    private String customerNumber;
    private String accountNumber;
    private String accountOpeningTime;
    private String action;

    public AccountOpeningLog() {
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
