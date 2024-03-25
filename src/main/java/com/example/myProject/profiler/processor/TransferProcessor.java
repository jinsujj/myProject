package com.example.myproject.profiler.processor;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myproject.common.domain.Bank;
import com.example.myproject.common.domain.Customer;
import com.example.myproject.common.response.log.TransferLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransferProcessor implements BaseProcessor{
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;
    private String receivingAccountHolder;
    private String receivingAccountNumber;
    private String receivingBank;
    private long transferAmount;
    private String transferTime;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        TransferLog transferLog = mapper.readValue(record.value(), TransferLog.class);

        customerNumber = transferLog.getCustomerNumber();
        receivingAccountHolder = transferLog.getReceivingAccountHolder();
        receivingAccountNumber = transferLog.getReceivingAccountNumber();
        receivingBank = transferLog.getReceivingBank();
        transferAmount = transferLog.getTransferAmount();
        transferTime = transferLog.getTransferTime();

        Optional<Customer> customerByNumber = bank.findCustomerByNumber(customerNumber);
        if(customerByNumber.isPresent()){
            Customer customer = customerByNumber.get();
            customer.addSession();
            customer.transfer(receivingBank, receivingAccountNumber, receivingAccountHolder, transferAmount,transferTime);
            System.out.println(transferLog.toJson());
        }
        else{
            System.out.println("'Transfer' Customer not found  : " + customerNumber);
        }
    }
}
