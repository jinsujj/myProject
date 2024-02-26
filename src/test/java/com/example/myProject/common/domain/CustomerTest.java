package com.example.myProject.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer(1L, "홍길동", "1990-01-01", "2023-01-01 00:00:00");
    }

    @Test
    void create_customer_and_verify_details() {
        assertEquals(1L, customer.getCustomerId());
        assertEquals("C1", customer.getCustomerNumber());
        assertEquals("홍길동", customer.getName());
        assertEquals("1990-01-01", customer.getDateOfBirth());
        assertEquals("2023-01-01 00:00:00", customer.getJoinDateTime());
        assertEquals(0, customer.getSessionCount());
    }

    @Test
    void testAdd_session_and_verify() {
        // when 
        customer.addSession();

        // then
        assertEquals(1, customer.getSessionCount());
    }

    @Test
    void testOpenAccount_and_verify() {
        // when
        customer.openAccount("123-456-789");

        // then
        assertNotNull(customer.getAccount());
        assertEquals("123-456-789", customer.getAccount().getAccountNumber());
    }

    @Test
    void testDeposit_and_verify_balance() {
        // given
        customer.openAccount("123-456-789");

        // when
        customer.deposit(1000);

        // then
        assertEquals(1000, customer.getAccount().getBalance());
    }

    @Test
    void testWithdraw_with_sufficient_balance() {
        // given
        customer.openAccount("123-456-789");
        customer.deposit(2000);

        // when , then
        assertTrue(customer.withdraw(1000));
        assertEquals(1000, customer.getAccount().getBalance());
    }

    @Test
    void testWithdraw_with_insufficient_balance() {
        // given
        customer.openAccount("123-456-789");

        // when 
        customer.deposit(500);

        // then
        assertFalse(customer.withdraw(1000)); // 잔액 부족
    }

    @Test
    void testTransfer_with_sufficient_balance() {
        // given
        customer.openAccount("123-456-789");

        // when
        customer.deposit(3000);

        // then
        assertTrue(customer.transfer("다른 은행", "987-654-321", "김철수", 1500));
        assertEquals(1500, customer.getAccount().getBalance());
    }

    @Test
    void testTtransfer_with_insufficient_balance() {
        // given
        customer.openAccount("123-456-789");

        // when
        customer.deposit(1000);

        // then
        assertFalse(customer.transfer("다른 은행", "987-654-321", "김철수", 1500)); // 잔액 부족
    }
}
