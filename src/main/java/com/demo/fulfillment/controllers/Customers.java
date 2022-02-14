package com.demo.fulfillment.controllers;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.repositories.CustomerRepository;
import com.demo.fulfillment.services.CustomerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class Customers {

    @Autowired
    CustomerRepository repository;

    @Autowired
    CustomerService service;

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List Customers",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Customer.class)) }),
            @ApiResponse(responseCode = "404", description = "Customers not found",
                    content = @Content) })
    @ResponseBody List<Customer> getCustomers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody Customer findCustomer(@PathVariable UUID id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.ALL_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created Random Customer",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Customer.class)) }),
            @ApiResponse(responseCode = "500", description = "Unable to create Customer") })
    @ResponseBody Customer createCustomer(Customer customer) {
        return service.addCustomers(Collections.singletonList(customer)).get(0);
    }

    @PostMapping(value = "/random", consumes = MediaType.ALL_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created Random Customer",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Customer.class)) }),
            @ApiResponse(responseCode = "500", description = "Unable to create Customer") })
    @ResponseBody Customer createRandomCustomer() {
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
