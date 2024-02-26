package com.example.myProject.customerProfiler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ExecutorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myProject.common.domain.Bank;

public class EventConsumerTest {
    private EventConsumer eventConsumer;
    private ExecutorService executorService;
    private int consumerCount =10;
    private Bank bank;

    @BeforeEach
    void setUp(){
        executorService = mock(ExecutorService.class);

        eventConsumer = new EventConsumer(consumerCount, bank);
        eventConsumer.setExecutorService(executorService);  
    }


    @Test
    void testConsume() throws InterruptedException {
        // when
        eventConsumer.consume();

        // then
        verify(executorService, times(consumerCount)).submit(any(ConsumerRunner.class));
    }
}
