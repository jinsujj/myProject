package com.example.myProject.common.financialLog.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DepositLog {
    private String customerNumber;
    private String depositAccountNumber;
    private long depositAmount;
    private String depositTime;

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DepositLog(String customerNumber, String depositAccountNumber, long depositAmount, LocalDateTime depositTime) {
        this.customerNumber = customerNumber;
        this.depositAccountNumber = depositAccountNumber;
        this.depositAmount = depositAmount;
        this.depositTime = depositTime.format(formatter);
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
}
