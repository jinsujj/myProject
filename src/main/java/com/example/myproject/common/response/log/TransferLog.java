package com.example.myproject.common.response.log;

import java.time.LocalDateTime;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TransferLog extends BaseLog {
    private final String customerNumber;
    private final String remittanceAccountNumber;
    private final String receivingBank;
    private final String receivingAccountNumber;
    private final String receivingAccountHolder;
    private final long transferAmount;
    private final String transferTime;
    private final String action;

    public TransferLog(){
        this.customerNumber = "";
        this.remittanceAccountNumber = "";
        this.receivingBank = "";
        this.receivingAccountNumber = "";
        this.receivingAccountHolder = "";
        this.transferAmount = 0;
        this.transferTime = "";
        this.action = FinancialAction.TRANSFER.name();
    }

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
        this.action = FinancialAction.TRANSFER.name();
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

    public String getAction() {
        return action;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
