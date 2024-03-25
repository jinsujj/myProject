package com.example.myproject.customerprofiler.processor;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.financialog.SignUpLog;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SignUpProcessorTest {
    private Bank bank;
    private Customer customer;
    private SignUpProcessor signUpProcessor;

    @BeforeEach
    void setUp(){
        bank = new Bank();
        signUpProcessor = new SignUpProcessor();
    }

    @Test
    void testProcess() throws JsonProcessingException {
        // given
        var customerNumber = "C1234";
        var signUpProcessLog = new SignUpLog(customerNumber, "name", "1993-01-01", "2024-02-26");
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key", signUpProcessLog.toJson());
        Optional<Customer> isCustomerExist = bank.findCustomerByNumber(customerNumber);

        // when
        signUpProcessor.process(record, bank);

        // then
        customer = bank.findCustomerByNumber(customerNumber).get();
        assertTrue(isCustomerExist.equals(Optional.empty()));
        assertTrue(customer.getCustomerNumber().equals(customerNumber));
        assertTrue(customer.getSessionCount() == 1);
    }
}
