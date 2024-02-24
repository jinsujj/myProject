package com.example.myProject.testDataGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.domain.FinancialAction;
import com.example.myProject.common.financialLog.v1.AccountOpeningLog;
import com.example.myProject.common.financialLog.v1.DepositLog;
import com.example.myProject.common.financialLog.v1.SessionStartLog;
import com.example.myProject.common.financialLog.v1.SignUpLog;
import com.example.myProject.common.financialLog.v1.TransferLog;
import com.example.myProject.common.financialLog.v1.WithdrawLog;
import com.example.myProject.testDataGenerator.util.EventProducer;
import com.example.myProject.testDataGenerator.util.RandomMaker;
import com.example.myProject.testDataGenerator.util.SessionManager;
import com.fasterxml.jackson.core.JsonProcessingException;

public class TestDataGenerator {
    private FinancialAction[] actions = { FinancialAction.DEPOSIT, FinancialAction.TRANSFER, FinancialAction.WITHDRAWAL };
    private final int customerCount;
    private final int simultaneousCustomer;
    private final int intervalDelay;
    private SessionManager sessionManager = new SessionManager();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    RandomMaker rMaker = new RandomMaker();
    Random random = new Random();
    Bank bank = new Bank();

    public TestDataGenerator(int customerCount, int simultaneousCustomer ,int intervalDelay) {
        this.customerCount = customerCount;
        this.simultaneousCustomer = simultaneousCustomer;     
        this.intervalDelay = intervalDelay;
    }

    // 고객 간 동시 거래 시뮬레이션 (거래 간 랜덤 시간 간격으로 실행)    
    public void simulateCustomerBehavior(){
        ExecutorService executorService = Executors.newFixedThreadPool(simultaneousCustomer);

        for (int i = 0; i < simultaneousCustomer; i++) {
            EventProducer producer = new EventProducer();
            executorService.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        long customerId = 1 + random.nextInt(customerCount);
                        SimulateSingleCustomer(customerId, producer);
                        // random (0 ~ 2) sec delay
                        Thread.sleep((long) (Math.random() * intervalDelay));
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        executorService.shutdown();
                        break;
                    }}}
            );
        }
    }

    // 단일 고객 거래 시뮬레이션 
    private void SimulateSingleCustomer(long customerId, EventProducer producer) throws JsonProcessingException {   
        // 세션 체크         
        if (!sessionManager.startSession(customerId))
            return; 

        try {
            Optional<Customer> findCustomer = bank.findCustomerById(customerId);
            
            if (findCustomer.isPresent()) {
                FinancialAction randomAction = actions[random.nextInt(actions.length)];
                Customer customer = findCustomer.get();
                sessionStartEvent(customer, producer);
                processFinancialAction(customer, randomAction, producer);
                
            } else {
                sessionStartEvent(customerId, producer);
                processNewMemberAction(customerId, producer);                
            }
        } finally {
            sessionManager.endSession(customerId); 
        }
    }

    // 고객 거래(입금,출금,이체) 처리
    private void processFinancialAction(Customer customer, FinancialAction action, EventProducer producer) throws JsonProcessingException {
        switch (action) {
            case DEPOSIT:
                long depositAmount = rMaker.generateAmount();
                customer.deposit(depositAmount);                
                depositEvent(customer, depositAmount, producer);
                break;

            case TRANSFER:
                String recevingBank = rMaker.generateBankName();
                String recevingAccount = rMaker.generateAccountNumber();
                String recevingHolder = rMaker.generateName();
                long transferAmount = rMaker.generateAmount();
                if (customer.transfer(recevingBank, recevingAccount, recevingHolder, transferAmount))
                    transferEvent(customer, recevingBank, recevingAccount, recevingHolder, transferAmount, producer);
                break;

            case WITHDRAWAL:
                long withdrawAmount = rMaker.generateAmount();
                if(customer.withdraw(withdrawAmount));
                    withdrawEvent(customer, withdrawAmount, producer);
                break;

            default:
                break;  
        }
    }

    // 고객 가입 & 계좌 개설
    private void processNewMemberAction(long customerId, EventProducer producer) throws JsonProcessingException {
        // customer signup
        Customer newCustomer = new Customer(customerId, rMaker.generateName(), 
                                            rMaker.generateBirth(), 
                                            LocalDateTime.now().format(formatter));
        bank.signupCustomer(newCustomer);
        signUpEvent(newCustomer, producer);

        // open account
        newCustomer.openAccount(rMaker.generateAccountNumber());
        openAccountEvent(newCustomer, producer);
    }


    
    // Kafka Producer에 이벤트 전송
    private void sessionStartEvent(long customerId, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.SESSION_START, String.valueOf(customerId),
                 new SessionStartLog("", LocalDateTime.now()).toJson());
    }

    private void sessionStartEvent(Customer customer, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.SESSION_START,String.valueOf(customer.getCustomerId()),
                new SessionStartLog(customer.getCustomerNumber(), LocalDateTime.now()).toJson());
    }

    private void signUpEvent(Customer customer, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.SIGNUP,String.valueOf(customer.getCustomerId()),
                new SignUpLog(customer.getCustomerNumber(),
                                customer.getName(),
                                customer.getDateOfBirth(),
                                customer.getJoinDateTime()).toJson());
                
    }

    private void openAccountEvent(Customer customer, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.OPEN_ACCOUNT,String.valueOf(customer.getCustomerId()),
                new AccountOpeningLog(customer.getCustomerNumber(),
                                        customer.getAccount().getAccountNumber(),
                                        LocalDateTime.now()).toJson());
    }

    public void depositEvent(Customer customer, long depositAmout, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.DEPOSIT,String.valueOf(customer.getCustomerId()),
                new DepositLog(customer.getCustomerNumber(), 
                                customer.getAccount().getAccountNumber(), 
                                depositAmout, 
                                LocalDateTime.now()).toJson());
    }

    
    public void transferEvent(Customer customer, String receivingBank, String receivingAccountNumber,
                String receivingAccountHolder, long transferAmount, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.TRANSFER, String.valueOf(customer.getCustomerId()),
                new TransferLog(customer.getCustomerNumber(),
                                customer.getAccount().getAccountNumber(),
                                receivingBank,
                                receivingAccountNumber, 
                                receivingAccountHolder, 
                                transferAmount, 
                                LocalDateTime.now()).toJson());
    }

    public void withdrawEvent(Customer customer, long withdrawAmount, EventProducer producer) throws JsonProcessingException {
        producer.send(FinancialAction.WITHDRAWAL, String.valueOf(customer.getCustomerId()),
                new WithdrawLog(customer.getCustomerNumber(), 
                                customer.getAccount().getAccountNumber(), 
                                withdrawAmount, 
                                LocalDateTime.now()).toJson());
    }
}
