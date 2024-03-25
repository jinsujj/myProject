package com.example.myproject.datagenerator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.concurrent.ExecutorService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Account;
import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.domain.FinancialAction;
import com.example.myproject.datagenerator.util.EventProducer;
import com.example.myproject.datagenerator.util.SessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class TestDataGeneratorTest {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private TestDataGenerator testDataGenerator;
    private ExecutorService executorService;
    private EventProducer producer;
    private Customer customer;
    private SessionManager sessionManager;
    private Bank bank;

    static int customerCount = 10;
    static int simultaneousCustomer = 5;
    static int intervalDelay = 1000;

    @BeforeEach
    void setUp(){
        executorService = mock(ExecutorService.class);
        producer = mock(EventProducer.class);

        sessionManager = new SessionManager();
        bank = new Bank();
        testDataGenerator = new TestDataGenerator(customerCount, simultaneousCustomer, intervalDelay);
        
        testDataGenerator.setExecutorService(executorService);
        testDataGenerator.setSessionManager(sessionManager);
        testDataGenerator.setBank(bank);
    }


    @Test
    void testSimulateCustomerBehavior() {
        // when 
        testDataGenerator.simulateCustomerBehavior();

        // then
        verify(executorService, times(simultaneousCustomer)).submit(any(Runnable.class));
    }

    @Test
    void testSimulateSingleCustomer_newCustomer() throws JsonProcessingException{
        // given 
        FinancialAction anyAction = FinancialAction.WITHDRAWAL;
        long customerId = 1L;

        // when 
        testDataGenerator.simulateSingleCustomer(customerId, anyAction, producer);

        // then 
        assertTrue(sessionManager.startSession(customerId));
        assertTrue(bank.findCustomerById(customerId).isPresent());
        // session_start, sign_up, open_account
        verify(producer, times(3)).send(anyString(), anyString()); 
    }

    @Test
    void testSimulateSingleCustomer_existingCustomer_deposit() throws JsonProcessingException{
        // given 
        FinancialAction depositAction = FinancialAction.DEPOSIT;
        long customerId = 1L;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        bank.signupCustomer(customer);

        // when
        testDataGenerator.simulateSingleCustomer(customerId, depositAction, producer);

        // then 
        assertTrue(sessionManager.startSession(customerId));
        assertTrue(customer.getAccount().getBalance() > 0);
        // session_start, deposit
        verify(producer, times(2)).send(anyString(), anyString()); 
    }

    @Test
    void testSimulateSingleCustomer_existingCustomer_transfer_with_proper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction transferAction = FinancialAction.TRANSFER;
        long customerId = 1L;
        long initAmount = 10000000;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);

        // when
        testDataGenerator.simulateSingleCustomer(customerId, transferAction, producer);

        // then
        assertTrue(sessionManager.startSession(customerId));
        assertTrue(customer.getAccount().getBalance() < initAmount);
        // session_start, transer
        verify(producer, times(2)).send(anyString(), anyString()); 
    }

    @Test
    void testSimulateSingleCustomer_existingCustomer_transfer_with_improper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction transferAction = FinancialAction.TRANSFER;
        long customerId = 1L;
        long initAmount = 500;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);
        
        // when
        testDataGenerator.simulateSingleCustomer(customerId, transferAction, producer);

        // then
        assertTrue(sessionManager.startSession(customerId));
        assertTrue(customer.getAccount().getBalance() == initAmount);
        // session_start
        verify(producer, times(1)).send(anyString(), anyString()); 
    }
    
    @Test
    void testSimulateSingleCustomer_existingCustomer_withdrawal_with_proper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction withdrawalAction = FinancialAction.WITHDRAWAL;
        long customerId = 1L;
        long initAmount = 10000000;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);

        // when
        testDataGenerator.simulateSingleCustomer(customerId, withdrawalAction, producer);

        // then
        assertTrue(sessionManager.startSession(customerId));
        assertTrue(customer.getAccount().getBalance() < initAmount);
        // session_start, withdrawal
        verify(producer, times(2)).send(anyString(), anyString()); 
    }

    @Test
    void testSimulateSingleCustomer_existingCustomer_withdrawal_with_improper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction transferAction = FinancialAction.WITHDRAWAL;
        long customerId = 1L;
        long initAmount = 500;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);
        
        // when
        testDataGenerator.simulateSingleCustomer(customerId, transferAction, producer);

        // then
        assertTrue(sessionManager.startSession(customerId));
        assertTrue(customer.getAccount().getBalance() == initAmount);
        // session_start
        verify(producer, times(1)).send(anyString(), anyString()); 
    }

    @Test
    void testProcessNewMemberAction() throws JsonProcessingException{
        // given
        long customerId = 1L;

        // when
        testDataGenerator.processNewMemberAction(customerId, producer);

        // then
        assertTrue(bank.findCustomerById(customerId).isPresent());

        Account account = bank.findCustomerById(customerId).get().getAccount();
        assertTrue(account.getAccountNumber().length() > 0);
        assertTrue(account.getBalance() == 0);
        // sign_up, open_account
        verify(producer, times(2)).send(anyString(), anyString()); 
    }


    @Test
    void testProcessFinancialAction_deposit() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction depositAction = FinancialAction.DEPOSIT;
        long customerId = 1L;
        long initAmount = 500;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);

        // when
        testDataGenerator.processFinancialAction(customer, depositAction, producer);

        // then
        assertTrue(customer.getAccount().getBalance() > initAmount);
        // deposit
        verify(producer, times(1)).send(anyString(), anyString()); 
    }

    @Test
    void testProcessFinancialAction_transfer_with_proper_amout() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction transferAction = FinancialAction.TRANSFER;
        long customerId = 1L;
        long initAmount = 10000000;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);

        // when
        testDataGenerator.processFinancialAction(customer, transferAction, producer);

        // then
        assertTrue(customer.getAccount().getBalance() < initAmount);
        // transfer
        verify(producer, times(1)).send(anyString(), anyString()); 
    }

    @Test
    void testProcessFinancialAction_transwer_with_improper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction transferAction = FinancialAction.TRANSFER;
        long customerId = 1L;
        long initAmount = 500;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);

        // when
        testDataGenerator.processFinancialAction(customer, transferAction, producer);

        // then
        assertTrue(customer.getAccount().getBalance() == initAmount);
        // no event
        verify(producer, times(0)).send(anyString(), anyString()); 
    }
    
    @Test
    void testProcessFinancialAction_withdraw_with_proper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction withdrawalAction = FinancialAction.WITHDRAWAL;
        long customerId = 1L;
        long initAmount = 10000000;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);

        // when
        testDataGenerator.processFinancialAction(customer, withdrawalAction, producer);

        // then
        assertTrue(customer.getAccount().getBalance() < initAmount);
        // withdrawal
        verify(producer, times(1)).send(anyString(), anyString()); 
    }

    @Test
    void testProcessFinancialAction_withdraw_with_improper_amount() throws JsonProcessingException{
        // given
        String eventTime = LocalDateTime.now().format(formatter);
        FinancialAction transferAction = FinancialAction.WITHDRAWAL;
        long customerId = 1L;
        long initAmount = 500;

        customer = new Customer(customerId, "name", "1993-01-01", "2024-01-01");
        customer.deposit(initAmount, eventTime);
        bank.signupCustomer(customer);
        
        // when
        testDataGenerator.processFinancialAction(customer, transferAction, producer);

        // then
        assertTrue(customer.getAccount().getBalance() == initAmount);
        // no event
        verify(producer, times(0)).send(anyString(), anyString()); 
    }
}
