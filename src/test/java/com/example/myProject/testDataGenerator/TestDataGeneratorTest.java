package com.example.myProject.testDataGenerator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ExecutorService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.testDataGenerator.util.EventProducer;
import com.example.myProject.testDataGenerator.util.SessionManager;

public class TestDataGeneratorTest {

    private TestDataGenerator testDataGenerator;
    private ExecutorService executorService;
    private SessionManager sessionManager;
    private Bank bank;
    private EventProducer producer;
    private Customer customer;

    static int customerCount = 10;
    static int simultaneousCustomer = 5;
    static int intervalDelay = 1000;

    @BeforeEach
    void setUp(){
        executorService = mock(ExecutorService.class);
        sessionManager = mock(SessionManager.class);
        bank = mock(Bank.class);
        producer = mock(EventProducer.class);
        customer = mock(Customer.class);

        testDataGenerator = new TestDataGenerator(customerCount, simultaneousCustomer, intervalDelay);
        testDataGenerator.setExecutorService(executorService);
        testDataGenerator.setSessionManager(sessionManager);
        testDataGenerator.setBank(bank);
        testDataGenerator.setEventProducer(producer);
    }


    @Test
    void testSimulateCustomerBehavior() {
        // when 
        testDataGenerator.simulateCustomerBehavior();

        // then
        verify(executorService, times(simultaneousCustomer)).submit(any(Runnable.class));
    }

    
}
