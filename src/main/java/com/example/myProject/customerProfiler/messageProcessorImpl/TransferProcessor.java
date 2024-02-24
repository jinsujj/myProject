package com.example.myProject.customerProfiler.messageProcessorImpl;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.financialLog.v1.TransferLog;
import com.example.myProject.customerProfiler.MessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransferProcessor implements MessageProcessor{
    private ObjectMapper mapper = new ObjectMapper();
    private String customerNumber;
    private String receivingAccountHolder;
    private String receivingAccountNumber;
    private String receivingBank;
    private long transferAmount;

    @Override
    public void process(ConsumerRecord<String, String> record, Bank bank) throws JsonProcessingException {
        TransferLog transferLog = mapper.readValue(record.value(), TransferLog.class);

        customerNumber = transferLog.getCustomerNumber();
        receivingAccountHolder = transferLog.getReceivingAccountHolder();
        receivingAccountNumber = transferLog.getReceivingAccountNumber();
        receivingBank = transferLog.getReceivingBank();
        transferAmount = transferLog.getTransferAmount();

        bank.findCustomerByNumber(customerNumber).ifPresent(customer -> {
            customer.addSession();

            customer.transfer(receivingBank, receivingAccountNumber, receivingAccountHolder, transferAmount);
        });

        System.out.println("Transfer: "+transferLog.toJson());
    }
}
