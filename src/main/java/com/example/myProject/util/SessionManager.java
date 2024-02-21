package com.example.myProject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager {
    private final ConcurrentHashMap<Long, AtomicInteger> activeSessions = new ConcurrentHashMap<>();

    // startSession 메소드를 만들어줘 이때 customerId 는 동시에 2개 이상의 세션을 시작하지 못하도록 해야한다.
    public boolean startSession(long customerId) {
        return activeSessions.putIfAbsent(customerId, new AtomicInteger(0)) == null;
    }

    public void endSession(long customerId) {
        activeSessions.remove(customerId);
    }


}
