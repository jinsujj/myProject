package com.example.myProject.TestDataGenerator.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
    private final ConcurrentHashMap<Long, AtomicInteger> activeSessions = new ConcurrentHashMap<>();

    // customerId 별 세션은 동시에 2개 이상의 세션을 갖지 못하도록 한다.
    public boolean startSession(long customerId) {
        return activeSessions.putIfAbsent(customerId, new AtomicInteger(0)) == null;
    }

    public void endSession(long customerId) {
        activeSessions.remove(customerId);
    }


}
