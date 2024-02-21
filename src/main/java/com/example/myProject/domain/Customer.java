package com.example.myProject.domain;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private long customerId;
    private String name;
    private String dateOfBirth;
    private List<Account> accounts;

    public Customer(long customerId, String name, String dateOfBirth) {
        this.customerId = customerId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.accounts = new ArrayList<>();
    }

    public void openAccount(String accountNumber) {
        Account newAccount = new Account(accountNumber, this.customerId);
        this.accounts.add(newAccount);
    }

    public void deposit(String accountNumber, long amount) {
        for(Account account : accounts) {
            if(account.getAccountNumber().equals(accountNumber)) {
                account.deposit(amount);
                break;
            }
        }
    }

    public boolean withdraw(String accountNumber, long amount) {
        for(Account account : accounts) {
            if(account.getAccountNumber().equals(accountNumber)) {
                if (account.getBalance() >= amount) {
                    account.withdraw(amount);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, long amount) {
        Account fromAccount = null;
        Account toAccount = null;
        for(Account account : accounts) {
            if(account.getAccountNumber().equals(fromAccountNumber)) {
                fromAccount = account;
            }
            if(account.getAccountNumber().equals(toAccountNumber)) {
                toAccount = account;
            }

            if (fromAccount != null && toAccount != null) 
                break;
        }

        if(fromAccount != null && toAccount != null) {
            synchronized (fromAccount) {
                fromAccount.withdraw(amount);
                synchronized (toAccount) {
                    toAccount.deposit(amount);
                }
            }
        }
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
