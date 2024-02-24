package com.example.myProject.common.financialLog.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WithdrawLog {
    private String customerNumber;
    private String withdrawAccountNumber;
    private long withdrawAmount;
    private String withdrawTime;

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public WithdrawLog(String customerNumber, String withdrawAccountNumber, long withdrawAmount, LocalDateTime withdrawTime) {
        this.customerNumber = customerNumber;
        this.withdrawAccountNumber = withdrawAccountNumber;
        this.withdrawAmount = withdrawAmount;
        this.withdrawTime = withdrawTime.format(formatter);
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
}
