package com.example.myProject.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
    private final ConcurrentHashMap<Long, AtomicInteger> activeSessions = new ConcurrentHashMap<>();

    // 특정 customerId 당 1개의 세션만 활성화되도록 합니다.     
    public boolean startSession(long customerId) {
        activeSessions.putIfAbsent(customerId, new AtomicInteger(0));
        if(activeSessions.get(customerId).incrementAndGet() == 1)
            return true;

        System.out.println("Customer " + customerId + " is already active");
        return false;
    }   

    public void endSession(long customerId) {
        AtomicInteger currentCount = activeSessions.get(customerId);
        if (currentCount != null) {
            int count = currentCount.decrementAndGet();
            if (count == 0) {
                activeSessions.remove(customerId);
            }
        }
    }

    public ConcurrentHashMap<Long, AtomicInteger> getActiveSessions() {
        return activeSessions;
    }
}
