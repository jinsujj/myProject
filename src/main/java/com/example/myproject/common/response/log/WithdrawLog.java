package com.example.myproject.common.response.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WithdrawLog {
    private String customerNumber;
    private String withdrawAccountNumber;
    private long withdrawAmount;
    private String withdrawTime;
    private String action;

    private transient ObjectMapper mapper = new ObjectMapper();
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public WithdrawLog() {
    }

    public WithdrawLog(String customerNumber, String withdrawAccountNumber, long withdrawAmount, LocalDateTime withdrawTime) {
        this.customerNumber = customerNumber;
        this.withdrawAccountNumber = withdrawAccountNumber;
        this.withdrawAmount = withdrawAmount;
        this.withdrawTime = withdrawTime.format(formatter);
        this.action = FinancialAction.WITHDRAWAL.name();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getWithdrawAccountNumber() {
        return withdrawAccountNumber;
    }

    public long getWithdrawAmount() {
        return withdrawAmount;
    }

    public String getWithdrawTime() {
        return withdrawTime;
    }

    public String getAction() {
        return action;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
