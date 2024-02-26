package com.example.myProject.common.domain;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private long customerId;
    private List<Transaction> transactions;
    private long balance;
    private long maxDepositAmount;
    private long maxWithdrawalAmount;
    private long maxTransferAmount;
    private long minDepositAmount;
    private long minWithdrawalAmount;
    private long minTransferAmount;

    public Account() {
        this.transactions = new ArrayList<>();
    }
    
    public Account(long customerId, String accountNumber) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.transactions = new ArrayList<>();
        this.balance = 0;
        this.maxDepositAmount = 0;
        this.maxWithdrawalAmount = 0;
        this.maxTransferAmount = 0;
        this.minDepositAmount = Long.MAX_VALUE;
        this.minWithdrawalAmount = Long.MAX_VALUE;
        this.minTransferAmount = Long.MAX_VALUE;
    }

    public void deposit(long amount, String eventTime) {
        this.balance += amount;
        addTransaction(FinancialAction.DEPOSIT, amount, eventTime);
    }

    public boolean withdraw(long amount, String eventTime) {
        if(this.balance < amount){ 
            return false;
        }
            
        this.balance -= amount;
        addTransaction(FinancialAction.WITHDRAWAL, amount, eventTime);
        return true;
    }

    public boolean transfer(String receivingBank, String receivingAccountNumber, String receivingAccountHolder, long amount, String eventTime) {
        if(this.balance < amount) {
            return false;
        }

        this.balance -= amount;
        addTransaction(FinancialAction.TRANSFER, amount,receivingBank, receivingAccountNumber, receivingAccountHolder, eventTime);
        return true;
    }

    // 거래 추가
    public void addTransaction(FinancialAction type, long amount, String eventTime) {
        transactions.add(new Transaction(type, amount, eventTime));
        checkAmountPerType(type, amount);

        // 최근 3건의 거래만 유지
        if (transactions.size() > 3) {
            transactions.remove(0);
        }
    }
    
    public void addTransaction(FinancialAction type, long amount, String receivingBank, String receivingAccountNumber, String receivingAccountHolder, String eventTime) {
        if (type != FinancialAction.TRANSFER) 
            return ;

        transactions.add(new Transaction(type, amount, eventTime, receivingBank, receivingAccountNumber, receivingAccountHolder));
        checkAmountPerType(type, amount);

        // 최근 3건의 거래만 유지
        if (transactions.size() > 3) {
            transactions.remove(0);
        }
    }

    // 거래별 최대 금액 갱신
    private void checkAmountPerType(FinancialAction type, long amount) {
        if (type == FinancialAction.DEPOSIT) {
            if (maxDepositAmount < amount) {
                maxDepositAmount = amount;
            }
            if (minDepositAmount > amount) {
                minDepositAmount = amount;
            }
        } else if (type == FinancialAction.WITHDRAWAL) {
            if (maxWithdrawalAmount < amount) {
                maxWithdrawalAmount = amount;
            }
            if (minWithdrawalAmount > amount) {
                minWithdrawalAmount = amount;
            }
        } else if (type == FinancialAction.TRANSFER) {
            if (maxTransferAmount < amount) {
                maxTransferAmount = amount;
            }
            if (minTransferAmount > amount) {
                minTransferAmount = amount;
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
        return maxWithdrawalAmount;
    }

    public long getMaxTransferAmount() {
        return maxTransferAmount;
    }

    public long getMinDepositAmount() {
        if (minDepositAmount == Long.MAX_VALUE) 
            return 0;
        
        return minDepositAmount;
    }

    public long getMinWithdrawalAmount() {
        if (minWithdrawalAmount == Long.MAX_VALUE) 
            return 0;

        return minWithdrawalAmount;
    }

    public long getMinTransferAmount() {
        if (minTransferAmount == Long.MAX_VALUE) 
            return 0;
            
        return minTransferAmount;
    }
}
