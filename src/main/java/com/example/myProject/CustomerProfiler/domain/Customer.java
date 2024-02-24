package com.example.myProject.customerProfiler.domain;

public class Customer {
    private long customerId;
    private String name;
    private String birthDate;
    private String joinDateTime;
    private long sessionCount;
    private Account account;

    public Customer(long customerId, String name, String birthDate, String joinDateTime) {
        this.customerId = customerId;
        this.name = name;
        this.birthDate = birthDate;
        this.joinDateTime = joinDateTime;
        this.sessionCount = 0;
        this.account = new Account();
    }

    public void addSession() {
        this.sessionCount++;
    }

    public void addAccount(Account account) {
        this.account = account;
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getJoinDateTime() {
        return joinDateTime;
    }

    public long getSessionCount() {
        return sessionCount;
    }

    public Account getAccount() {
        return account;
    }
}
