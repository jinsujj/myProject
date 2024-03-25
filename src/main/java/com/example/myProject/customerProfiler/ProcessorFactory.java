package com.example.myproject.customerprofiler;

import java.util.HashMap;
import java.util.Map;

import com.example.myproject.common.domain.FinancialAction;
import com.example.myproject.customerprofiler.processor.DepositProcessor;
import com.example.myproject.customerprofiler.processor.OpenAccountProcessor;
import com.example.myproject.customerprofiler.processor.SessionStartProcessor;
import com.example.myproject.customerprofiler.processor.TransferProcessor;
import com.example.myproject.customerprofiler.processor.WithdrawalProcessor;
import com.example.myproject.customerprofiler.processor.SignUpProcessor;

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
