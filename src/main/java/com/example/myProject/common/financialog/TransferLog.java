package com.example.myproject.common.financialog;

import java.time.LocalDateTime;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TransferLog extends BaseLog{
    private String customerNumber;
    private String remittanceAccountNumber;
    private String receivingBank;
    private String receivingAccountNumber;
    private String receivingAccountHolder;
    private long transferAmount;
    private String transferTime;
    private String action;

    public TransferLog() {
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
