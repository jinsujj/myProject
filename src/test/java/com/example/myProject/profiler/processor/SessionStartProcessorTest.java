package com.example.myproject.profiler.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.financialog.SessionStartLog;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

public class SessionStartProcessorTest {
    private Bank bank;
    private Customer customer;
    private SessionStartProcessor sessionStartProcessor;

    @BeforeEach
    void setUp(){
        bank = new Bank();
        sessionStartProcessor = new SessionStartProcessor();
    }

    @Test
    void testProcess() throws JsonProcessingException {
        // given
        var customerNumber = "C1234";
        var now = LocalDateTime.now();
        customer = new Customer(customerNumber, "name", "", "");

        var nowSessionCount = customer.getSessionCount();
        bank.signupCustomer(customer);

        var sessionStartLog = new SessionStartLog(customerNumber, now);
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key", sessionStartLog.toJson());

        // when
        sessionStartProcessor.process(record, bank);

        // then
        Customer findCustomer = bank.findCustomerByNumber(customerNumber).get();
        assertTrue(findCustomer.getSessionCount() == nowSessionCount +1);
    }
}
