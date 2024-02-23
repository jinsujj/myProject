package com.example.myProject;

import com.example.myProject.CustomerProfiler.util.EventConsumer;
import com.example.myProject.TestDataGenerator.TestDataGenerator;

public class App 
{   
    // Test Data Generator Config
    private static final int CUSTOMER_COUNT = 50000;    
    private static final int SIMULATANEOUS_CUSTOMER = 100;


    public static void main( String[] args )
    {
        if(args.length == 0 || args[0].equals("generator")){
            TestDataGenerator testDataGenerator = new TestDataGenerator(CUSTOMER_COUNT, SIMULATANEOUS_CUSTOMER);
            testDataGenerator.simulateCustomerBehavior();
        }
        else if (args[0].equals("profiler")){
            EventConsumer eventConsumer = new EventConsumer();
            eventConsumer.consumeEvent();
        }
    }
}
