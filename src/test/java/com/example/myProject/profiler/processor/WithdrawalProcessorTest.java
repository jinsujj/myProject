package com.example.myproject.profiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.financialog.WithdrawLog;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WithdrawalProcessorTest {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
        var eventTime = LocalDateTime.now().format(formatter);
        var customerNumber = "C1234";
        var accountNumber = "111-222-333";
        var initAmount = 3000;
        var withdrawalAmount = 1000;
        var withdrawLog = new WithdrawLog(customerNumber, accountNumber, withdrawalAmount, LocalDateTime.now());
        var record = new ConsumerRecord<>("topic", 0, 0L, "key", withdrawLog.toJson());
        customer = new Customer(customerNumber, "name", "", "");
        customer.deposit(initAmount, eventTime);
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
