package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.financialLog.v1.TransferLog;
import com.fasterxml.jackson.core.JsonProcessingException;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

public class TransferProcessorTest {
    private Bank bank;
    private Customer customer;
    private TransferProcessor transferProcessor;

    @BeforeEach
    void setUp(){
        bank = new Bank();
        transferProcessor = new TransferProcessor();
    }

    @Test
    void testProcess() throws JsonProcessingException {
        // given
        var initAmount = 3000;
        var customerNumber = "C1234";
        var remittanceAccountNumber = "111-222-333";
        var receivingBank = "KB은행";   
        var receivingAccountNumber ="123-321-543";
        var receivingAccountHolder = "홍길동";
        var transferAmount = 1000;
        var transferLog = new TransferLog(
            customerNumber, remittanceAccountNumber, receivingBank, receivingAccountNumber, receivingAccountHolder, transferAmount, LocalDateTime.now());
        
        var record = new ConsumerRecord<>("topic", 0, 0L, "key", transferLog.toJson());
        customer = new Customer(customerNumber, "name", "", "");
        customer.deposit(initAmount);

        var nowSessionCount = customer.getSessionCount();
        bank.signupCustomer(customer);

        // when
        transferProcessor.process(record, bank);

        // then
        Customer findCustomer = bank.findCustomerByNumber(customerNumber).get();
        assertTrue(findCustomer.getSessionCount() == nowSessionCount +1);
        assertTrue(findCustomer.getAccount().getBalance() == (initAmount - transferAmount)) ;
    }
}
