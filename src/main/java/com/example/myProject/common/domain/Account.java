package com.example.myProject.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private long customerId;
    private List<Transaction> transactions;
    private long balance;
    private long maxDepositAmount;
    private long maxwithdrawalAmount;
    private long maxTransferAmount;

    public Account() {
    }
    
    public Account(long customerId, String accountNumber) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.maxDepositAmount = 0;
        this.maxwithdrawalAmount = 0;
        this.maxTransferAmount = 0;
    }

    public void deposit(long amount) {
        this.balance += amount;
        System.out.println("CustomerId: " + customerId + " Deposit: " + amount + " Current balance: " + this.balance);
        addTransaction(FinancialAction.DEPOSIT, amount);
    }

    public boolean withdraw(long amount) {
        if(this.balance < amount){ 
            System.out.println("CustomerId: " + customerId+ " Insufficient balance for withdrawl.");
            return false;
        }
            
        this.balance -= amount;
        addTransaction(FinancialAction.WITHDRAWAL, amount);
        return true;
    }

    public boolean transfer(String receivingBank, String receivingAccountNumber, String receivingAccountHolder, long amount) {
        if(this.balance < amount) {
            System.out.println("CustomerId: "+ customerId+ " Insufficient balance for transfer." +" Current balance: " + this.balance + " Transfer amount: " + amount);
            return false;
        }

        this.balance -= amount;
        addTransaction(FinancialAction.TRANSFER, amount);
        System.out.println("CustomerId: "+ customerId+ " Transfer: " + amount + " Current balance: " + this.balance + " Receiving bank: " + receivingBank + " Receiving account number: " + receivingAccountNumber + " Receiving account holder: " + receivingAccountHolder);
        return true;
    }

    // 거래 추가
    public void addTransaction(FinancialAction type, long amount) {
        transactions.add(new Transaction(type, amount));
        checkMaxAmountPerType(type, amount);

        // 최근 3건의 거래만 유지
        if (transactions.size() > 3) {
            transactions.remove(0);
        }
    }

    // 거래별 최대 금액 갱신
    private void checkMaxAmountPerType(FinancialAction type, long amount) {
        if (type == FinancialAction.DEPOSIT) {
            if (maxDepositAmount < amount) {
                maxDepositAmount = amount;
            }
        } else if (type == FinancialAction.WITHDRAWAL) {
            if (maxwithdrawalAmount < amount) {
                maxwithdrawalAmount = amount;
            }
        } else if (type == FinancialAction.TRANSFER) {
            if (maxTransferAmount < amount) {
                maxTransferAmount = amount;
            }
        }
    }

    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public long getCustomerId() {
        return customerId;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getBalance() {
        return balance;
    }

    public long getMaxDepositAmount() {
        return maxDepositAmount;
    }

    public long getMaxwithdrawalAmount() {
        return maxwithdrawalAmount;
    }

    public long getMaxTransferAmount() {
        return maxTransferAmount;
    }
}
