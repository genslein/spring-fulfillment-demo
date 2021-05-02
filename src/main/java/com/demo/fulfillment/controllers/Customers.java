package com.demo.fulfillment.controllers;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class Customers {

    @Autowired
    CustomerRepository repository;

    @GetMapping
    @ResponseBody List<Customer> getCustomers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody Customer findCustomer(@PathVariable UUID id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.ALL_VALUE)
    @ResponseBody Customer createCustomer() {
        String email = "chEdwardCheese" +
                (new Random().nextInt()) +
                "@someplace.com";

        return repository.save(Customer.builder()
                .firstName("Charles")
                .lastName("Edward Cheese")
                .email(email)
                .addressOne("123 Smith Lane")
                .city("New York")
                .state("NY")
                .zipCode(10011)
                .build());
    }
}
