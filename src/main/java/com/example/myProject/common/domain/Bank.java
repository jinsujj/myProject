package com.example.myproject.common.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.example.myproject.common.responsedto.CustomerInfo;


public class Bank {
    private Map<Long, Customer> customers;

    public Bank() {
        this.customers = new ConcurrentHashMap<>();
    }

    // 고객 등록
    public void signupCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        System.out.println("Customer " + customer.getCustomerId() + " is signed up");
    }

    // 고객 조회
    public Optional<Customer> findCustomerByNumber(String customerNumber){
        long customerId = Long.parseLong(customerNumber.substring(1));
        
        return Optional.ofNullable(customers.get(customerId));
    }

    public Optional<Customer> findCustomerById(long customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }
    
    // 계좌 조회
    public Optional<Account> findAccountByCustomerName(String customerNumber) {
        Optional<Customer> findCustomer = findCustomerByNumber(customerNumber);

        if(!findCustomer.isPresent()) 
            return Optional.empty();
        
        return Optional.ofNullable(findCustomer.get().getAccount());
    }

    // 고객 목록 조회
    public List<CustomerInfo> customerList(int page, int size) {
        int start = (page - 1) * size;
        int end = start + size;

        List<CustomerInfo> customerInfoList = new ArrayList<>();
        List<Customer> filteredCustomers = new ArrayList<>(customers.values());
        end = Math.min(end, filteredCustomers.size());

        for (int i = start; i < end; i++) {
            Customer customer = filteredCustomers.get(i);
            CustomerInfo customerInfo = new CustomerInfo(customer);
            customerInfoList.add(customerInfo);
        }

        return customerInfoList;
    }

    public String customerSize() {
        return String.valueOf(customers.size());
    }
}
