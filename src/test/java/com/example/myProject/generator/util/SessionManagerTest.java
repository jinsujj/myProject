package com.example.myproject.datagenerator.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SessionManagerTest {
    private SessionManager sessionManager;

    @BeforeEach
    void setUp(){
        sessionManager = new SessionManager();
    }

    @Test
    void testStartSessionWithNewCustomer(){
        assertTrue(sessionManager.startSession(1L));
    }

    @Test
    void testStartSessionWithExistingCustomer(){
        sessionManager.startSession(1L);
        assertTrue(!sessionManager.startSession(1L));
    }

    @Test
    void testStartSessionAfterEndSession(){
        sessionManager.startSession(1L);
        sessionManager.endSession(1L);
        assertTrue(sessionManager.startSession(1L));
    }

}
