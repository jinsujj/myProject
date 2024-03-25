package com.example.myproject.common.response.log;

import java.time.LocalDateTime;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class WithdrawLog extends BaseLog {
    private final String customerNumber;
    private final String withdrawAccountNumber;
    private final long withdrawAmount;
    private final String withdrawTime;
    private final String action;

    public WithdrawLog(){
        this.customerNumber = "";
        this.withdrawAccountNumber = "";
        this.withdrawAmount = 0;
        this.withdrawTime = "";
        this.action = "";
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
