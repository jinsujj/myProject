package com.example.myproject.common.response.dto;

import java.util.Collections;
import java.util.List;

import com.example.myproject.common.domain.Account;
import com.example.myproject.common.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AccountInfo extends BaseInfo {
    private final String accountNumber;
    private final long balance;
    private final long maxDepositAmount;
    private final long maxwithdrawalAmount;
    private final long maxTransferAmount;
    private final long minDepositAmount;
    private final long minWithdrawalAmount;
    private final long minTransferAmount;
    private final List<Transaction> transactions;

    public AccountInfo(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.maxDepositAmount = account.getMaxDepositAmount();
        this.maxwithdrawalAmount = account.getMaxwithdrawalAmount();
        this.maxTransferAmount = account.getMaxTransferAmount();
        this.minDepositAmount = account.getMinDepositAmount();
        this.minWithdrawalAmount = account.getMinWithdrawalAmount();
        this.minTransferAmount = account.getMinTransferAmount();
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
        return Collections.unmodifiableList(transactions);
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
