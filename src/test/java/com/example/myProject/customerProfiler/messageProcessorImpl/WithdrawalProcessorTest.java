package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.financialLog.v1.WithdrawLog;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

public class WithdrawalProcessorTest {
    private Bank bank;
    private Customer customer;
    private WithdrawalProcessor withdrawalProcessor;

    @BeforeEach
    void setUp(){
        bank = new Bank();
        withdrawalProcessor = new WithdrawalProcessor();
    }

    @Test
    void testProcess() throws JsonProcessingException {
        // given
        var customerNumber = "C1234";
        var accountNumber = "111-222-333";
        var initAmount = 3000;
        var withdrawalAmount = 1000;
        var withdrawLog = new WithdrawLog(customerNumber, accountNumber, withdrawalAmount, LocalDateTime.now());
        var record = new ConsumerRecord<>("topic", 0, 0L, "key", withdrawLog.toJson());
        customer = new Customer(customerNumber, "name", "", "");
        customer.deposit(initAmount);
        bank.signupCustomer(customer);
        var nowSessionCount = customer.getSessionCount();

        // when
        withdrawalProcessor.process(record, bank);

        // then
        Customer findCustomer = bank.findCustomerByNumber(customerNumber).get();
        assertTrue(findCustomer.getSessionCount() == nowSessionCount +1);
        assertTrue(findCustomer.getAccount().getBalance() == (initAmount - withdrawalAmount)) ;
    }
}