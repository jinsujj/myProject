package com.example.myProject.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "123-456-789");
    }

    @Test
    void testDeposit() {
        // given
        long initialBalance = account.getBalance();
        long depositAmount = 1000;

        // when
        account.deposit(depositAmount);

        // then
        assertEquals(initialBalance + depositAmount, account.getBalance());
        assertEquals(depositAmount, account.getMaxDepositAmount());
        assertEquals(depositAmount, account.getMinDepositAmount());
    }

    @Test
    void testWithdraw_with_sufficient_balance() {
        // given
        long depositAmount = 2000;
        account.deposit(depositAmount);

        // when, then
        assertTrue(account.withdraw(1000));
        assertEquals(1000, account.getBalance());
        assertEquals(1000, account.getMaxwithdrawalAmount());
        assertEquals(1000, account.getMinWithdrawalAmount());
    }

    @Test
    void testWithdraw_with_insufficient_balance() {
        // given
        long withdrawAmount = 500;

        // when,then
        assertFalse(account.withdraw(withdrawAmount));
        assertEquals(0, account.getBalance());
    }

    @Test
    void testTransfer_with_sufficient_balance() {
        // given
        account.deposit(3000);

        // when,then
        assertTrue(account.transfer("Other Bank", "987-654-321", "John Doe", 1500));
        assertEquals(1500, account.getBalance());
        assertEquals(1500, account.getMaxTransferAmount());
        assertEquals(1500, account.getMinTransferAmount());
    }

    @Test
    void testTransfer_with_insufficient_balance() {
        // when, then
        assertFalse(account.transfer("Other Bank", "987-654-321", "John Doe", 500));
        assertEquals(0, account.getBalance());
    }

    @Test
    void testTransaction_history_limit() {
        // given
        account.deposit(100);
        account.deposit(200);
        account.withdraw(50);
        account.deposit(300);
        // when
        List<Transaction> transactions = account.getTransactions();

        // then
        assertEquals(3, transactions.size());
    }
}
