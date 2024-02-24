package com.example.myProject.common.domain;

public class Customer {
    private long customerId;
    private String customerNumber;
    private String name;
    private String birthDate;
    private String joinDateTime;
    private long sessionCount;
    private Account account;

    // testDataGenerator
    public Customer(long customerId, String name, String birthDate, String joinDateTime) {
        this.customerId = customerId;
        this.customerNumber = "C" + customerId;
        this.name = name;
        this.birthDate = birthDate;
        this.joinDateTime = joinDateTime;
        this.sessionCount = 0;
        this.account = new Account();
    }

    // consumerRunner
    public Customer(String customerNumber, String name, String birthDate, String joinDateTime) {
        this.customerId = Long.parseLong(customerNumber.substring(1));
        this.customerNumber = customerNumber;
        this.name = name;
        this.birthDate = birthDate;
        this.joinDateTime = joinDateTime;
        this.sessionCount = 1;
        this.account = new Account();
    }

    
    public void addSession() {
        this.sessionCount++;
    }

    public void openAccount(String accountNumber) {
        Account account = new Account(customerId, accountNumber);
        this.account = account;
    }

    public void deposit(long amount) {
        this.account.deposit(amount);
    }

    public boolean withdraw(long amount) {
        if(account.withdraw(amount))
            return true;

        return false;
    }

    public boolean transfer(String receivingBank, String receivingAccountNumber, String receivingAccountHolder, long amount) {
        if (account.transfer(receivingBank, receivingAccountNumber, receivingAccountHolder, amount))
            return true;

        return false;
    }

                  
    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
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
