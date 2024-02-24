package com.example.myProject.common.financialLog.v1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransferLog {
    private String customerNumber;
    private String remittanceAccountNumber;
    private String receivingBank;
    private String receivingAccountNumber;
    private String receivingAccountHolder;
    private long transferAmount;
    private String transferTime;

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public TransferLog(String customerNumber, String remittanceAccountNumber, String receivingBank,
            String receivingAccountNumber, String receivingAccountHolder, long transferAmount,
            LocalDateTime transferTime) {
        this.customerNumber = customerNumber;
        this.remittanceAccountNumber = remittanceAccountNumber;
        this.receivingBank = receivingBank;
        this.receivingAccountNumber = receivingAccountNumber;
        this.receivingAccountHolder = receivingAccountHolder;
        this.transferAmount = transferAmount;
        this.transferTime = transferTime.format(formatter);
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getRemittanceAccountNumber() {
        return remittanceAccountNumber;
    }

    public String getReceivingBank() {
        return receivingBank;
    }

    public String getReceivingAccountNumber() {
        return receivingAccountNumber;
    }

    public String getReceivingAccountHolder() {
        return receivingAccountHolder;
    }

    public long getTransferAmount() {
        return transferAmount;
    }

    public String getTransferTime() {
        return transferTime;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}
