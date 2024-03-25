package com.example.myproject.common.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.responsedto.CustomerInfo;


public class BankTest {
    private Bank bank;
    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        customer1 = new Customer(1L, "홍길동","1993-01-01","2024-02-26");
        customer2 = new Customer(2L, "카리나", "1993-01-01", "2024-02-26");
        
        bank.signupCustomer(customer1);
        bank.signupCustomer(customer2);
    }

    @Test
    void test_signup_customer_and_findCustomerById() {
        // given
        Optional<Customer> foundCustomer = bank.findCustomerById(1L);

        // when, then
        assertTrue(foundCustomer.isPresent());
        assertEquals(customer1, foundCustomer.get());
    }

    @Test
    void testFindCustomerByNumber() {
        // when
        Optional<Customer> foundCustomer = bank.findCustomerByNumber("C1");

        // then
        assertTrue(foundCustomer.isPresent());
        assertEquals(customer1, foundCustomer.get());
    }
    
    @Test
    void testFindAccountByCustomerName() {
        // when
        Optional<Account> foundAccount = bank.findAccountByCustomerName("C1");

        // then
        assertTrue(foundAccount.isPresent());
        assertEquals(customer1.getAccount(), foundAccount.get());
    }

    @Test
    void testCustomerList_pagination() {
        // when
        List<CustomerInfo> customerInfoList = bank.customerList(1, 1);

        // then
        assertEquals(1, customerInfoList.size());
        assertEquals(customer1.getCustomerNumber(), customerInfoList.get(0).getCustomerNumber());
    }

    @Test
    void testCustomerSize() {
        assertEquals("2", bank.customerSize());
    }
}
