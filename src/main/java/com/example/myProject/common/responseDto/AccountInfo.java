package com.example.myProject.common.responseDto;

import java.util.List;

import com.example.myProject.common.domain.Account;
import com.example.myProject.common.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountInfo {
    private String accountNumber;
    private long balance;
    private long maxDepositAmount;
    private long maxwithdrawalAmount;
    private long maxTransferAmount;
    private List<Transaction> transactions;

    private transient ObjectMapper mapper = new ObjectMapper();

    public AccountInfo(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.maxDepositAmount = account.getMaxDepositAmount();
        this.maxwithdrawalAmount = account.getMaxwithdrawalAmount();
        this.maxTransferAmount = account.getMaxTransferAmount();
        this.transactions = account.getTransactions();
    }

    public String getAccountNumber() {
        return accountNumber;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
