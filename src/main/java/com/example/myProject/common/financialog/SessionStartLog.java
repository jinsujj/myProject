package com.example.myproject.common.financialog;

import com.example.myproject.common.domain.FinancialAction;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;

public class SessionStartLog extends BaseLog {
    private String customerNumber;
    private String sessionTime;
    private String action;

    public SessionStartLog() {
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
