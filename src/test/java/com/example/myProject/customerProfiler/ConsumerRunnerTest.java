package com.example.myProject.customerProfiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myProject.common.domain.Bank;

import java.util.Properties;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.TopicPartition;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;

public class ConsumerRunnerTest {
    private KafkaConsumer<String, String> mockConsumer;
    private Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
    private ConsumerRunner consumerRunner;
    private Bank bank;

    @BeforeEach
    void setUp(){
        mockConsumer = mock(KafkaConsumer.class);
        Properties props = new Properties();
        bank = new Bank();
        consumerRunner = new ConsumerRunner("topic", "groupId", props, bank);
    }


    @Test
    void testHandleCommitFailuer() throws FileNotFoundException, IOException{
        // given 
        offsets.put(new TopicPartition("topic", 0), new OffsetAndMetadata(123L));
        var exception = new Exception("Test commit failure");
        var filePath = "failed_consumer_offset.txt";

        // when
        consumerRunner.handleCommitFailure(offsets, exception);

        // then
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            assertNotNull(line);
            assertTrue(line.contains("topic"));
            assertTrue(line.contains("0"));
            assertTrue(line.contains("123"));
        }
    }
}
