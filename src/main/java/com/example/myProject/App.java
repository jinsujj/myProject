package com.example.myProject;

import com.example.myProject.util.SessionManager;

public class App 
{   
    // 고객 수
    private static final int CUSTOMER_COUNT = 500;
    // 동시에 행동하는 고객 수
    private static final int SIMULATANEOUS_CUSTOMER = 100;
    private static final SessionManager sessionManager = new SessionManager();

    public static void main( String[] args )
    {
        if(args[0].equals("generator")){
            TestDataGenerator testDataGenerator = new TestDataGenerator(
                CUSTOMER_COUNT, SIMULATANEOUS_CUSTOMER, sessionManager);
            testDataGenerator.simulateCustomerBehavior();
        }
        else {
            System.out.println("Invalid argument");
        }
    }
}
