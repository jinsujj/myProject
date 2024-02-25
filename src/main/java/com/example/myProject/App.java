package com.example.myProject;

import com.example.myProject.common.domain.Account;
import com.example.myProject.common.domain.Bank;
import com.example.myProject.common.domain.Customer;
import com.example.myProject.common.responseDto.AccountInfo;
import com.example.myProject.common.responseDto.CustomerInfo;
import com.example.myProject.customerProfiler.EventConsumer;
import com.example.myProject.testDataGenerator.TestDataGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App 
{   
    // Test Data Generator Config
    private static final int CUSTOMER_COUNT = 50000;        // 5만명 고객
    private static final int SIMULATANEOUS_CUSTOMER = 100;  // 100명의 고객 동시 행동
    private static final int INTERVAL_DELAY = 2000;         // (0 ~ 2000) 밀리초 간 랜덤 간격

    // Profiler Config                                      // '동시 고객' 보다 10% 더 많은 컨슈머
    private static final int CONSUMER_COUNT = (int)(SIMULATANEOUS_CUSTOMER * 1.1); 

    public static void main( String[] args ) throws InterruptedException
    {   
        // profiler 모드로 실행
        if (args.length == 0 || args[0].equals("profiler")) {
            Bank bank = new Bank();

            EventConsumer eventConsumer = new EventConsumer(CONSUMER_COUNT,bank);
            eventConsumer.consume();
            sparkApi(bank);
            return;
        }

        // generator 모드로 실행
        if (args[0].equals("generator")) {
            TestDataGenerator generator = new TestDataGenerator(
                CUSTOMER_COUNT, SIMULATANEOUS_CUSTOMER,INTERVAL_DELAY);

            generator.simulateCustomerBehavior();
            return;
        }
    }


    private static void sparkApi(Bank bank) {
        port(8080);
        // 전체 고객 목록 조회
        get("/customers", (req, res) -> {
            ObjectMapper mapper = new ObjectMapper();
            int page = Integer.parseInt(req.queryParams("page") != null ? req.queryParams("page") : "1");
            int size = Integer.parseInt(req.queryParams("size") != null ? req.queryParams("size") : "100");

            String totalCustomers = bank.customerSize();
            List<CustomerInfo> customerList = bank.customerList(page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("totalCustomers", totalCustomers);
            response.put("customers", customerList);
            response.put("currentPage", page);
            response.put("pageSize", size);

            res.type("application/json");
            return mapper.writeValueAsString(response);
        });

        // 고객 상세 조회
        get("/customer/:customerNumber", (req, res) -> {
            String customerNumber = req.params(":customerNumber");
            Optional<Customer> findCustomer = bank.findCustomerByNumber(customerNumber);
            
            if (!findCustomer.isPresent()) {
                res.status(404);
                return "Customer not found";
            }

            CustomerInfo customerInfo = new CustomerInfo(findCustomer.get());
            res.type("application/json");
            return customerInfo.toJson();
            
        }); 
        
        // 고객 계좌 조회
        get("/customer/:customerNumber/account", (req, res) -> {
            String customerNumber = req.params(":customerNumber");
            Optional<Account> findAccount = bank.findAccountByCustomerName(customerNumber);
            
            if (!findAccount.isPresent()) {
                res.status(404);
                return "Account not found";
            }
            
            AccountInfo accountInfo = new AccountInfo(findAccount.get());
            if(accountInfo.getAccountNumber().isEmpty()) {
                res.status(404);
                return "Account not found";
            }

            res.type("application/json");
            return accountInfo.toJson(); 
            
        });
    }
}
