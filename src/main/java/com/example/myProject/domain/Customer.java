package com.example.myProject.domain;

public class Customer {
    private long customerId;
    private String customerNumber;
    private String name;
    private String dateOfBirth;
    private Account account;

    public Customer(long customerId, String name, String dateOfBirth) {
        this.customerId = customerId;
        this.customerNumber = "C-" + customerId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.account = null;
    }

    public void openAccount(String accountNumber) {
        Account newAccount = new Account(accountNumber, this.customerId);
        this.account = newAccount;
    }

    public void deposit(long amount) {
        account.deposit(amount);
    }

    public boolean withdraw(long amount) {
        if (account.getBalance() < amount){
            System.out.println("Insufficient balance for transfer.");
            return false;
        }

        account.withdraw(amount);
        return true;
    }

    public boolean transfer(long amount, Account remittanceAccountNumber, String receivingBank, String receivingAccountNumber, String recevingAccountHolder) {
        if(withdraw(amount))
            return false;
            
        return true;
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
        return dateOfBirth;
    }

    public Account getAccounts() {
        return account;
    }
}
