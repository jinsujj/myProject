package com.example.myProject.common.financialLog.v1;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

public class SessionStartLog {
    private String customerNumber;
    private String sessionTime;

    private transient ObjectMapper mapper = new ObjectMapper();
    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SessionStartLog() {
    }

    public SessionStartLog(String customerNumber, LocalDateTime sesstionTime) {
        this.customerNumber = customerNumber;
        this.sessionTime = sesstionTime.format(formatter);
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
