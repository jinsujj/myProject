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
import com.example.myProject.testDataGenerator.util.EventProducer;
import com.example.myProject.testDataGenerator.util.EventLogMaker;
import com.example.myProject.testDataGenerator.util.RandomMaker;
import com.example.myProject.testDataGenerator.util.SessionManager;

public class TestDataGenerator {
    private FinancialAction[] actions = { FinancialAction.DEPOSIT, FinancialAction.TRANSFER, FinancialAction.WITHDRAWAL };
    private int customerCount;
    private int simultaneousCustomer;
    private SessionManager sessionManager = new SessionManager();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    RandomMaker rMaker = new RandomMaker();
    EventLogMaker logMaker = new EventLogMaker();
    Random random = new Random();
    Bank bank = new Bank();

    public TestDataGenerator(int customerCount, int simultaneousCustomer) {
        this.customerCount = customerCount;
        this.simultaneousCustomer = simultaneousCustomer;     
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
                        Thread.sleep((long) (Math.random() * 2000));
                    } catch (Exception e) {
                        executorService.shutdown();
                        break;
                    }}}
            );
        }
    }

    // 단일 고객 거래 시뮬레이션 
    private void SimulateSingleCustomer(long customerId, EventProducer producer) {   
        // 세션 체크         
        if (!sessionManager.startSession(customerId))
            return; 

        try {
            Optional<Customer> findCustomer = bank.findCustomer(customerId);
            
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
    private void processFinancialAction(Customer customer, FinancialAction action, EventProducer producer) {
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
    private void processNewMemberAction(long customerId, EventProducer producer) {
        // customer signup
        Customer newCustomer = new Customer(customerId, rMaker.generateName(), rMaker.generateBirth(), 
                LocalDateTime.now().format(formatter));
        bank.signupCustomer(newCustomer);
        signUpEvent(newCustomer, producer);

        // open account
        newCustomer.openAccount(rMaker.generateAccountNumber());
        openAccountEvent(newCustomer, producer);
    }


    // Kafka Producer에 이벤트 전송
    private void sessionStartEvent(long customerId, EventProducer producer) {
        producer.send(FinancialAction.SESSION_START,
                String.valueOf(customerId),
                logMaker.logSessionStart("", LocalDateTime.now()));
    }

    private void sessionStartEvent(Customer customer, EventProducer producer) {
        producer.send(FinancialAction.SESSION_START,
                String.valueOf(customer.getCustomerId()),
                logMaker.logSessionStart(customer.getCustomerNumber(), LocalDateTime.now()));
    }

    private void signUpEvent(Customer customer, EventProducer producer) {
        producer.send(FinancialAction.SIGNUP,
                String.valueOf(customer.getCustomerId()),
                logMaker.logSignUp(customer.getCustomerNumber(), 
                                    customer.getName(),
                                    customer.getDateOfBirth(), 
                                    LocalDateTime.now()));
    }

    private void openAccountEvent(Customer customer, EventProducer producer) {
        producer.send(FinancialAction.OPEN_ACCOUNT,
                String.valueOf(customer.getCustomerId()),
                logMaker.logAccountOpening(customer.getCustomerNumber(), 
                                            customer.getAccount().getAccountNumber(),
                                            LocalDateTime.now()));
    }

    public void depositEvent(Customer customer, long depositAmout, EventProducer producer) {
        producer.send(FinancialAction.DEPOSIT,
                String.valueOf(customer.getCustomerId()),
                logMaker.logDeposit(customer.getCustomerNumber(), 
                                    customer.getAccount().getAccountNumber(),
                                    depositAmout, LocalDateTime.now()));
    }

    
    public void transferEvent(Customer customer, String receivingBank, String receivingAccountNumber,
                String receivingAccountHolder, long transferAmount, EventProducer producer) {
        producer.send(FinancialAction.TRANSFER,
                String.valueOf(customer.getCustomerId()),
                logMaker.logTransfer(customer,
                                     customer.getAccount().getAccountNumber(),
                                     receivingBank,
                                     receivingAccountNumber, 
                                     receivingAccountHolder,
                                     transferAmount, LocalDateTime.now()));
    }

    public void withdrawEvent(Customer customer, long withdrawAmount, EventProducer producer) {
        producer.send(FinancialAction.WITHDRAWAL,
                String.valueOf(customer.getCustomerId()),
                logMaker.logWithdrawal(customer.getCustomerNumber(),
                                        customer.getAccount().getAccountNumber(),
                                        withdrawAmount,     
                                        LocalDateTime.now()));
    }
}
