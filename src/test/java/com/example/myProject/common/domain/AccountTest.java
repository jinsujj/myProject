package com.example.myproject.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Account;
import com.example.myproject.common.domain.Transaction;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class AccountTest {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "123-456-789");
    }

    @Test
    void testDeposit() {
        // given
        String eventTime =LocalDateTime.now().format(formatter);
        long initialBalance = account.getBalance();
        long depositAmount = 1000;

        // when
        account.deposit(depositAmount, eventTime);

        // then
        assertEquals(initialBalance + depositAmount, account.getBalance());
        assertEquals(depositAmount, account.getMaxDepositAmount());
        assertEquals(depositAmount, account.getMinDepositAmount());
    }

    @Test
    void testWithdraw_with_sufficient_balance() {
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        long depositAmount = 2000;
        account.deposit(depositAmount,eventTime);

        // when, then
        assertTrue(account.withdraw(1000, LocalDateTime.now().format(formatter)));
        assertEquals(1000, account.getBalance());
        assertEquals(1000, account.getMaxwithdrawalAmount());
        assertEquals(1000, account.getMinWithdrawalAmount());
    }

    @Test
    void testWithdraw_with_insufficient_balance() {
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        long withdrawAmount = 500;

        // when,then
        assertFalse(account.withdraw(withdrawAmount, eventTime));
        assertEquals(0, account.getBalance());
    }

    @Test
    void testTransfer_with_sufficient_balance() {
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        account.deposit(3000,eventTime);

        // when,then
        assertTrue(account.transfer(
            "Other Bank", "987-654-321", "John Doe", 1500, LocalDateTime.now().format(formatter)));
        assertEquals(1500, account.getBalance());
        assertEquals(1500, account.getMaxTransferAmount());
        assertEquals(1500, account.getMinTransferAmount());
    }

    @Test
    void testTransfer_with_insufficient_balance() {
        // given
        String eventTime = LocalDateTime.now().format(formatter);

        // when, then
        assertFalse(account.transfer("Other Bank", "987-654-321", "John Doe", 500, eventTime));
        assertEquals(0, account.getBalance());
    }

    @Test
    void testTransaction_history_limit() {
        // given
        String eventTime = LocalDateTime.now().format(formatter);

        account.deposit(100, eventTime);
        account.deposit(200, LocalDateTime.now().format(formatter));
        account.withdraw(50, LocalDateTime.now().format(formatter));
        account.deposit(300, LocalDateTime.now().format(formatter));
        // when
        List<Transaction> transactions = account.getTransactions();

        // then
        assertEquals(3, transactions.size());
    }
}
