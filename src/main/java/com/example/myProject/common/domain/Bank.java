package com.example.myProject.common.domain;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private Map<Long, Customer> customers;

    public Bank() {
        this.customers = new ConcurrentHashMap<>();
    }

    public void signupCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public Optional<Customer> findCustomer(long customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    public String customerSize() {
        return String.valueOf(customers.size());
    }
}
