package com.example.myproject.datagenerator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class EventProducerTest {

    private static final String TOPIC = "FinancialEvents";
    private EventProducer eventProducer;
    private MockProducer<String, String> mockProducer;

    @BeforeEach
    void setUp(){
        mockProducer = new MockProducer<>(true, new StringSerializer(), new StringSerializer());
        eventProducer = new EventProducer(mockProducer);
    }

    @Test
    void testSend() {
        // given 
        String key = "100";
        String value = "log1";

        // when 
        eventProducer.send(key, value);

        // then
        assertFalse(mockProducer.history().isEmpty());
        ProducerRecord<String, String> sentRecord = mockProducer.history().get(0);
        assertEquals(TOPIC, sentRecord.topic());
        assertEquals(key, sentRecord.key());
        assertEquals(value, sentRecord.value());
    }

    @AfterEach
    void tearDown(){
        eventProducer.close();
    }
}
