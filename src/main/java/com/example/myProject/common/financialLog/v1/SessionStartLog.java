package com.example.myProject.common.financialLog.v1;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SessionStartLog {
    private String customerNumber;
    private String sesstionTime;

    private transient DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public SessionStartLog(String customerNumber, LocalDateTime sesstionTime) {
        this.customerNumber = customerNumber;
        this.sesstionTime = sesstionTime.format(formatter);
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getSesstionTime() {
        return sesstionTime;
    }
}
