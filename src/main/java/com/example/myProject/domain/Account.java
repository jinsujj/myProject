package com.example.myProject.domain;

public class Account {
    private String accountNumber;
    private long balance;
    private long customerId;

    public Account(String accountNumber,long customerId) {
        this.customerId = customerId;   
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    public void deposit(long amount) {
        this.balance += amount;
    }

    public void withdraw(long amount) {
        this.balance -= amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getBalance() {
        return balance;
    }

    public long getCustomerId() {
        return customerId;
    }

}
