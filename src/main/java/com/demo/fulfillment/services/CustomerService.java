package com.demo.fulfillment.services;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.models.subtypes.CustomerOrder;
import com.demo.fulfillment.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository repository;

    public List<CustomerOrder> getLatestCustomerOrders() {
        return repository.getCustomerLatestOrders();
    }

    public List<Customer> addCustomers(List<Customer> customers) {
       return repository.saveAll(customers);
    }

    public void deleteCustomers(List<Customer> customers) {
        repository.deleteAll(customers);
    }

    public void deleteAllCustomers(){
        repository.deleteAll();
    }
}
