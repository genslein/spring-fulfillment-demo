package com.demo.fulfillment.services;

import com.demo.fulfillment.BaseITest;
import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.models.subtypes.CustomerOrder;
import com.demo.fulfillment.models.subtypes.OrderItem;
import com.demo.fulfillment.repositories.CustomerRepository;
import com.demo.fulfillment.repositories.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerServiceITest extends BaseITest {

    @Autowired
    OrderRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerService service;

    @AfterEach
    public void cleanup() {
        repository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void getEachCustomerOrder_returnsListOfPairs() {
        List<Customer> customersList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            customersList.add(Customer.builder().email("customer_" + i)
                    .firstName("Townsperson" + i)
                    .lastName("Simpson")
                    .addressOne("100" + i + " Sherry Lane")
                    .city("New York")
                    .state("NY")
                    .zipCode(10024)
                    .build());
        }
        customersList = customerRepository.saveAll(customersList);

        List<OrderItem> firstList = new ArrayList<>();
        firstList.add(OrderItem.builder()
                .unitPrice(29.99)
                .sku("BLK-MED-G123-SEA")
                .quantity(1)
                .build());
        firstList.add(OrderItem.builder()
                .unitPrice(50.49)
                .sku("BLK-MED-C457-OAK")
                .quantity(2)
                .build());

        List<OrderItem> secondList = new ArrayList<>();
        secondList.add(OrderItem.builder()
                .unitPrice(1049.99)
                .sku("BLK-LRG-D667-DELL")
                .quantity(1)
                .build());

        List<Order> startingOrders = new ArrayList<>();
        List<Order> laterOrders = new ArrayList<>();

        for (Customer customer : customersList) {
            startingOrders.add(Order.builder()
                    .customerId(customer.getId())
                    .items(firstList)
                    .build());

            laterOrders.add(Order.builder()
                    .customerId(customer.getId())
                    .items(secondList)
                    .build());
        }

        startingOrders = repository.saveAll(startingOrders);
        List<CustomerOrder> firstCustomerOrders = service.getLatestCustomerOrders();

        assertThat(firstCustomerOrders.size()).isEqualTo(startingOrders.size());

        // Check that all orders loaded successfully
        List<UUID> actual = firstCustomerOrders.stream().map(f -> f.getOrder().getId()).sorted().collect(Collectors.toList());
        assertThat(actual).isEqualTo(startingOrders.stream().map(Order::getId).sorted().collect(Collectors.toList()));

        laterOrders = repository.saveAll(laterOrders);
        List<CustomerOrder> secondCustomerOrders = service.getLatestCustomerOrders();

        // Check that all orders loaded successfully
        actual = secondCustomerOrders.stream().map(f -> f.getOrder().getId()).sorted().collect(Collectors.toList());
        assertThat(actual).containsAll(laterOrders.stream().map(Order::getId).sorted().collect(Collectors.toList()));
    }
}
