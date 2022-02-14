package com.demo.fulfillment.controllers;

import com.demo.fulfillment.BaseITest;
import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.services.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomersITest extends BaseITest {
    @Autowired
    CustomerService service;

    @AfterEach
    public void cleanup() {
        service.deleteAllCustomers();
    }

    @Test
    public void testGet_returns_200_with_array_Customers() {
        List<Customer> customersList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            customersList.add(Customer.builder().email("customer_" + i)
                    .firstName("Townsperson" + i)
                    .lastName("Simpson")
                    .addressOne("100" + i + " Sherry Lane")
                    .city("New York")
                    .state("NY")
                    .zipCode(10024)
                    .build());
        }
        service.addCustomers(customersList);

        List<Customer> actual = given
                .log()
                .all()
                .when()
                .get( "/customers")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().body().jsonPath().getList("", Customer.class);

        assertThat(actual).isNotEmpty();
        assertThat(actual.size()).isEqualTo(10);
    }
}
