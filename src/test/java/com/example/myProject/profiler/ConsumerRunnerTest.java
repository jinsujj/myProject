package com.example.myproject.profiler;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.myproject.common.domain.Bank;

import java.util.Properties;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.TopicPartition;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConsumerRunnerTest {
    private Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
    private ConsumerRunner consumerRunner;
    private Bank bank;

    @BeforeEach
    void setUp(){
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
