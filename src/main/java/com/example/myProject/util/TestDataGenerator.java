package com.example.myProject.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.myProject.domain.Customer;

public class TestDataGenerator {
    private static final int CUSTOMER_COUNT = 50000;
    private static final int SIMULATANEOUS_CUSTOMER = 100;


    public void simulateCustomerBehavior(){
        ExecutorService executorService = Executors.newFixedThreadPool(SIMULATANEOUS_CUSTOMER);

        for (int i = 1; i <= CUSTOMER_COUNT; i++){
            final long customerId = i;

            executorService.submit(() -> {
                while(true){
                    try{
                        SimulateSingleCustomer(customerId);
                        Thread.sleep((long)(Math.random() * 1000));
                    }
                    catch(InterruptedException e){
                        executorService.shutdown();
                        break;
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public void SimulateSingleCustomer(long customerId){
        SessionManager sessionManager = new SessionManager();
        
        if (sessionManager.startSession(customerId)) {
            try{
                Customer customer = new Customer(customerId, null, null);
                customer.openAccount("123-456-789");
            }
            finally{
                sessionManager.endSession(customerId);
            }
        }
        else{
            System.out.println("Customer " + customerId + " is already active");
        }
    }
}
