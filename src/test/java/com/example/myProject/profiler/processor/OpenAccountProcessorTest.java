package com.example.myproject.customerprofiler.processor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.financialog.AccountOpeningLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;

public class OpenAccountProcessorTest {
    private Bank bank;
    private Customer customer;
    private OpenAccountProcessor openAccountProcessor;

    @BeforeEach
    void setUp(){
        bank = new Bank();
        openAccountProcessor = new OpenAccountProcessor();
    }

    @Test
    void testProcess() throws JsonProcessingException {
        // given
        var customerNumber = "C1234";
        var accountNumber = "111-222-333";
        customer = new Customer(customerNumber, "name", "", "");

        var nowSessionCount = customer.getSessionCount();
        bank.signupCustomer(customer);

        var accountOpeningLog = new AccountOpeningLog(customerNumber, accountNumber, LocalDateTime.now());
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key", accountOpeningLog.toJson());
        
        // when
        openAccountProcessor.process(record, bank);

        // then
        Customer findCustomer = bank.findCustomerByNumber(customerNumber).get();
        assertTrue(findCustomer.getSessionCount() == nowSessionCount +1);
        assertTrue(findCustomer.getAccount().getAccountNumber().equals(accountNumber));
        assertTrue(findCustomer.getAccount().getBalance() == 0);
    }
}
