package com.example.myproject.common.response.dto;

import java.util.List;

import com.example.myproject.common.domain.Account;
import com.example.myproject.common.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AccountInfo extends BaseInfo {
    private String accountNumber;
    private long balance;
    private long maxDepositAmount;
    private long maxwithdrawalAmount;
    private long maxTransferAmount;
    private long minDepositAmount;
    private long minWithdrawalAmount;
    private long minTransferAmount;
    private List<Transaction> transactions;

    public AccountInfo(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.maxDepositAmount = account.getMaxDepositAmount();
        this.maxwithdrawalAmount = account.getMaxwithdrawalAmount();
        this.maxTransferAmount = account.getMaxTransferAmount();
        this.transactions = account.getTransactions();
        this.minDepositAmount = account.getMinDepositAmount();
        this.minWithdrawalAmount = account.getMinWithdrawalAmount();
        this.minTransferAmount = account.getMinTransferAmount();
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

    public long getMinDepositAmount() {
        return minDepositAmount;
    }

    public long getMinWithdrawalAmount() {
        return minWithdrawalAmount;
    }

    public long getMinTransferAmount() {
        return minTransferAmount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
