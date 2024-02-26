package com.example.myProject.customerProfiler.messageProcessorImpl;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.financialLog.v1.WithdrawLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WithdrawalProcessor implements MessageProcessor {
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;
    private long withdrawAmount;
    private String withdrawTime;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        WithdrawLog withdrawLog = mapper.readValue(record.value(), WithdrawLog.class);

        customerNumber = withdrawLog.getCustomerNumber();
        withdrawAmount = withdrawLog.getWithdrawAmount();
        withdrawTime = withdrawLog.getWithdrawTime();

        Optional<Customer> customerByNumber = bank.findCustomerByNumber(customerNumber);
        if(customerByNumber.isPresent()){
            Customer customer = customerByNumber.get();
            customer.addSession();
            customer.withdraw(withdrawAmount, withdrawTime);
            System.out.println(withdrawLog.toJson());
        }
        else{
             System.out.println("'Withdrawal' Customer not found  : " + customerNumber);
        }
    }
}
