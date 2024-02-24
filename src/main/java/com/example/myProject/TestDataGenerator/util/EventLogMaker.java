package com.example.myProject.testDataGenerator.util;

import java.time.LocalDateTime;

import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.financialLog.v1.AccountOpeningLog;
import com.example.myProject.common.financialLog.v1.DepositLog;
import com.example.myProject.common.financialLog.v1.SessionStartLog;
import com.example.myProject.common.financialLog.v1.SignUpLog;
import com.example.myProject.common.financialLog.v1.TransferLog;
import com.example.myProject.common.financialLog.v1.WithdrawLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class EventLogMaker {
    ObjectMapper mapper = new ObjectMapper();

    // 세션 시작 로그 생성
    public String logSessionStart(String customerNumber, LocalDateTime sessionTime) {
        SessionStartLog sessionStartLog = new SessionStartLog(customerNumber, sessionTime);
        try {
            return mapper.writeValueAsString(sessionStartLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 가입 로그 생성
    public String logSignUp(String customerNumber, String customerName, String dateOfBirth,
             LocalDateTime registrationTime) {
        SignUpLog signUpLog = new SignUpLog(customerNumber, customerName, dateOfBirth, registrationTime);
        try {
            return mapper.writeValueAsString(signUpLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 계좌 개설 로그 생성
    public String logAccountOpening(String customerNumber, String accountNumber, 
            LocalDateTime accountOpeningTime) {
        AccountOpeningLog accountOpeningLog = new AccountOpeningLog(customerNumber, accountNumber, accountOpeningTime);
        try {
            return mapper.writeValueAsString(accountOpeningLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 입금 로그 생성
    public String logDeposit(String customerNumber, String depositAccountNumber, 
            long depositAmount, LocalDateTime depositTime) {
        DepositLog depositLog = new DepositLog(customerNumber, depositAccountNumber, depositAmount, depositTime);
        try {
            return mapper.writeValueAsString(depositLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 출금 로그 생성
    public String logWithdrawal(String customerNumber, String withdrawAccountNumber,
            long withdrawAmount,LocalDateTime withdrawalTime) {
        WithdrawLog withdrawLog = new WithdrawLog(customerNumber, withdrawAccountNumber, withdrawAmount, withdrawalTime);
        try {
            return mapper.writeValueAsString(withdrawLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 이체 로그 생성
    public String logTransfer(
            Customer customer, String remittanceAccountNumber, String receivingBank,
            String receivingAccountNumber, String receivingAccountHolder, 
            long transferAmount, LocalDateTime transferTime) {
        TransferLog transferLog = new TransferLog(customer.getCustomerNumber(),
                                                    remittanceAccountNumber,
                                                    receivingBank,
                                                    receivingAccountNumber, 
                                                    receivingAccountHolder, 
                                                    transferAmount, 
                                                    transferTime);
        try {   
            return mapper.writeValueAsString(transferLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
