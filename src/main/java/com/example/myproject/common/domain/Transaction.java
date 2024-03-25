package com.example.myproject.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Transaction {
    private FinancialAction type;
    private long amount;
    private String eventTime;

    @JsonInclude(Include.NON_NULL)
    private String receivingBank;
    @JsonInclude(Include.NON_NULL)
    private String receivingAccountNumber;
    @JsonInclude(Include.NON_NULL)
    private String receivingAccountHolder;

    public Transaction(FinancialAction type, long amount, String eventTime) {
        this.type = type;
        this.amount = amount;
        this.eventTime = eventTime;
    }

    public Transaction(FinancialAction type, long amount, String eventTime, String receivingBank, String receivingAccountNumber, String receivingAccountHolder) {
        this.type = type;
        this.amount = amount;
        this.eventTime = eventTime;
        this.receivingBank = receivingBank;
        this.receivingAccountNumber = receivingAccountNumber;
        this.receivingAccountHolder = receivingAccountHolder;
    }

    public String getType() {
        return type.name();
    }

    public long getAmount() {
        return amount;
    }

    public String getEventTime() {
        return eventTime;
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
}

