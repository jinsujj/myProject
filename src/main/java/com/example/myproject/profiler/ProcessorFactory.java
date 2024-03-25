package com.example.myproject.profiler;

import java.util.HashMap;
import java.util.Map;

import com.example.myproject.common.domain.FinancialAction;
import com.example.myproject.profiler.processor.DepositProcessor;
import com.example.myproject.profiler.processor.BaseProcessor;
import com.example.myproject.profiler.processor.OpenAccountProcessor;
import com.example.myproject.profiler.processor.SessionStartProcessor;
import com.example.myproject.profiler.processor.SignUpProcessor;
import com.example.myproject.profiler.processor.TransferProcessor;
import com.example.myproject.profiler.processor.WithdrawalProcessor;

public class ProcessorFactory {
    private final Map<FinancialAction, BaseProcessor> processorMap = new HashMap<>();

    public ProcessorFactory() {
        processorMap.put(FinancialAction.SESSION_START, new SessionStartProcessor());
        processorMap.put(FinancialAction.SIGNUP, new SignUpProcessor());
        processorMap.put(FinancialAction.OPEN_ACCOUNT, new OpenAccountProcessor());
        processorMap.put(FinancialAction.DEPOSIT, new DepositProcessor());
        processorMap.put(FinancialAction.WITHDRAWAL, new WithdrawalProcessor());
        processorMap.put(FinancialAction.TRANSFER, new TransferProcessor());
    }  
    
    public BaseProcessor getProcessor(FinancialAction action) {
        return processorMap.get(action);
    }
}
