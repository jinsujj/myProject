package com.example.myProject.customerProfiler.domain;

public class Transaction {
    private FinancialAction type;
    private long amount;

    public Transaction(FinancialAction type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type.name();
    }

    public long getAmount() {
        return amount;
    }
}
