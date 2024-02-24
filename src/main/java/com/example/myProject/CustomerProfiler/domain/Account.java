package com.example.myProject.customerProfiler.domain;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private List<Transaction> transactions;
    private String accountNumber;
    private long balance;
    private long maxDepositAmount;
    private long maxwithdrawalAmount;
    private long maxTransferAmount;

    public Account() {
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.maxDepositAmount = 0;
        this.maxwithdrawalAmount = 0;
        this.maxTransferAmount = 0;
    }

    public void deposit(long amount) {
        balance += amount;
        addTransaction(FinancialAction.DEPOSIT, amount);
    }

    public void withdraw(long amount) {
        balance -= amount;
        addTransaction(FinancialAction.WITHDRAWAL, amount);
    }

    public void transfer(Account targetAccount, long amount) {
        balance -= amount;
        addTransaction(FinancialAction.TRANSFER, amount);
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

    // 최대 금액 갱신
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

    public long getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
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
