package com.example.myProject.customerProfiler;

import java.util.HashMap;
import java.util.Map;
import com.example.myProject.common.domain.FinancialAction;
import com.example.myProject.customerProfiler.messageProcessorImpl.DepositProcessor;
import com.example.myProject.customerProfiler.messageProcessorImpl.OpenAccountProcessor;
import com.example.myProject.customerProfiler.messageProcessorImpl.SessionStartProcessor;
import com.example.myProject.customerProfiler.messageProcessorImpl.SignUpProcessor;
import com.example.myProject.customerProfiler.messageProcessorImpl.TransferProcessor;
import com.example.myProject.customerProfiler.messageProcessorImpl.WithdrawalProcessor;

public class ProcessorFactory {
    private final Map<FinancialAction, MessageProcessor> processorMap = new HashMap<>();

    public ProcessorFactory() {
        processorMap.put(FinancialAction.SESSION_START, new SessionStartProcessor());
        processorMap.put(FinancialAction.SIGNUP, new SignUpProcessor());
        processorMap.put(FinancialAction.OPEN_ACCOUNT, new OpenAccountProcessor());
        processorMap.put(FinancialAction.DEPOSIT, new DepositProcessor());
        processorMap.put(FinancialAction.WITHDRAWAL, new WithdrawalProcessor());
        processorMap.put(FinancialAction.TRANSFER, new TransferProcessor());
    }  
    
    public MessageProcessor getProcessor(FinancialAction action) {
        return processorMap.get(action);
    }
}
