package com.example.myProject.TestDataGenerator.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.myProject.TestDataGenerator.domain.Customer;


public class LogMaker {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 세션 시작 로그 생성
    public String logSessionStart(String customerNumber, LocalDateTime sessionTime) {
        return String.format("{\"customerNumber\": \"%s\", \"sessionTime\": \"%s\"}", customerNumber, sessionTime.format(formatter));
    }

    // 가입 로그 생성
    public String logSignUp(Customer customer,LocalDateTime registrationTime) {
        return String.format(
                "{\"customerNumber\": \"%s\", \"customerName\": \"%s\", \"dateOfBirth\": \"%s\", \"registrationTime\": \"%s\"}",
                customer.getCustomerNumber(), customer.getName(), customer.getDateOfBirth(), registrationTime.format(formatter));
    }

    // 계좌 개설 로그 생성
    public String logAccountOpening(Customer customer, LocalDateTime accountOpeningTime) {
        return String.format("{\"customerNumber\": \"%s\", \"accountNumber\": \"%s\", \"accountOpeningTime\": \"%s\"}",
                customer.getCustomerNumber(), customer.getAccounts().getAccountNumber(), accountOpeningTime.format(formatter));
    }

    // 입금 로그 생성
    public String logDeposit(Customer customer, LocalDateTime depositTime) {
        return String.format(
                "{\"customerNumber\": \"%s\", \"depositAccountNumber\": \"%s\", \"depositAmount\": %d, \"depositTime\": \"%s\"}",
                customer.getCustomerNumber(), 
                customer.getAccounts().getAccountNumber(), 
                customer.getAccounts().getBalance(), 
                depositTime.format(formatter));
    }

    // 출금 로그 생성
    public String logWithdrawal(Customer customer,LocalDateTime withdrawalTime) {
        return String.format(
                "{\"customerNumber\": \"%s\", \"withdrawalAccountNumber\": \"%s\", \"withdrawalAmount\": %d, \"withdrawalTime\": \"%s\"}",
                customer.getCustomerNumber(), 
                customer.getAccounts().getAccountNumber(), 
                customer.getAccounts().getBalance(), 
                withdrawalTime.format(formatter));
    }

    // 이체 로그 생성
    public String logTransfer(
            Customer customer, String remittanceAccountNumber, String receivingBank,
            String receivingAccountNumber, String receivingAccountHolder, 
            long transferAmount, LocalDateTime transferTime) {
        return String.format(
                "{\"customerNumber\": \"%s\", \"remittanceAccountNumber\": \"%s\", \"receivingBank\": \"%s\", \"receivingAccountNumber\": \"%s\", \"receivingAccountHolder\": \"%s\", \"transferAmount\": %d, \"transferTime\": \"%s\"}",
                customer.getCustomerNumber(), customer.getAccounts().getAccountNumber(), 
                receivingBank, receivingAccountNumber, receivingAccountHolder,
                transferAmount, transferTime.format(formatter));
    }
}
