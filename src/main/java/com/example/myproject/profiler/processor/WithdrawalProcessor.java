package com.example.myproject.profiler.processor;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.response.log.WithdrawLog;
import com.fasterxml.jackson.core.JsonProcessingException;

public class WithdrawalProcessor extends BaseProcessor {
    private String customerNumber;
    private long withdrawAmount;
    private String withdrawTime;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        WithdrawLog withdrawLog = this.mapper.readValue(record.value(), WithdrawLog.class);

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
