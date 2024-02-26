package com.example.myProject.common.domain;

public class Transaction {
    private FinancialAction type;
    private long amount;
    private String eventTime;

    public Transaction(FinancialAction type, long amount, String eventTime) {
        this.type = type;
        this.amount = amount;
        this.eventTime = eventTime;
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
}

