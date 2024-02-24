package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.financialLog.v1.WithdrawLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WithdrawalProcessor implements MessageProcessor {
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;
    private long withdrawAmount;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        WithdrawLog withdrawLog = mapper.readValue(record.value(), WithdrawLog.class);

        customerNumber = withdrawLog.getCustomerNumber();
        withdrawAmount = withdrawLog.getWithdrawAmount();

        bank.findCustomerByNumber(customerNumber).ifPresent(customer -> {
            customer.addSession();
            customer.withdraw(withdrawAmount);
        });
        
        System.out.println("Withdrawal: "+withdrawLog.toJson());
    }
}
