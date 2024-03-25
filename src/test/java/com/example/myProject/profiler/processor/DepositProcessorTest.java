package com.example.myproject.profiler.processor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.financialog.DepositLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;

public class DepositProcessorTest {
    private DepositProcessor depositProcessor;
    private Bank bank;
    private Customer customer;

    @BeforeEach
    void setUp(){
        bank = new Bank();
        depositProcessor = new DepositProcessor();
    }

    @Test
    void testProcessFinancialAction_withdraw_with_proper_amount() throws JsonProcessingException {
        // given
        var customerNumber = "C1234";
        var accountNumber = "111-222-333";
        var depositAmount = 1000;
        var depositLog = new DepositLog(customerNumber, accountNumber, depositAmount, LocalDateTime.now());
        var record = new ConsumerRecord<>("topic", 0, 0L, "key", depositLog.toJson());
        customer = new Customer(customerNumber, "name", "", "");
        
        var nowSessionCount = customer.getSessionCount();
        bank.signupCustomer(customer);

        // when
        depositProcessor.process(record, bank);

        // then
        Customer findCustomer = bank.findCustomerByNumber(customerNumber).get();
        assertTrue(findCustomer.getSessionCount() == nowSessionCount +1) ;
        assertTrue(findCustomer.getAccount().getBalance() == depositAmount) ;
    }
}
