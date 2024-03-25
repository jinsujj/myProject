package com.example.myproject.profiler;


import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.myproject.common.domain.Bank;

public class EventConsumer {
    private final String financialEventsTopic = "FinancialEvents";
    private final int consumerCount;
    private ExecutorService executor;
    private Bank bank;
    private Properties props = new Properties();

    public EventConsumer(int consumerCount, Bank bank) {
        this.bank = bank;
        this.consumerCount = consumerCount;
        this.executor = Executors.newFixedThreadPool(consumerCount);
    }

    // for test code
    protected void setExecutorService(ExecutorService executor) {
        this.executor = executor;
    }

    public void consume() throws InterruptedException {
        String groupId = "group_" + financialEventsTopic;
        int count = consumerCount;

        for (int i = 0; i < count; i++) {
            ConsumerRunner consumerRunner = new ConsumerRunner(financialEventsTopic, groupId, props, bank);
            executor.submit(consumerRunner);
        }
    }
}
