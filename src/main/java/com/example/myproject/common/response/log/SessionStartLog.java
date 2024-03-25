package com.example.myproject.common.response.log;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;

public class SessionStartLog extends BaseLog {
    private final String customerNumber;
    private final String sessionTime;
    private final String action;

    public SessionStartLog(){
        this.customerNumber = "";
        this.sessionTime = "";
        this.action = FinancialAction.SESSION_START.name();
    }

    public SessionStartLog(String customerNumber, LocalDateTime sesstionTime) {
        this.customerNumber = customerNumber;
        this.sessionTime = sesstionTime.format(formatter);
        this.action = FinancialAction.SESSION_START.name();
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public String getAction() {
        return action;
    }

    public String toJson() throws JsonProcessingException {
        return mapper.writeValueAsString(this);
    }
}
