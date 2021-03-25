package com.demo.fulfillment.services;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.repositories.OrderRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<Order> searchOrders(Predicate predicate) {
        return (List<Order>) orderRepository.findAll(predicate);
    }

    public List<Pair<Customer, Order>> getEachCustomerFirstOrder() {
        List<Pair<Customer, Order>> firstOrders = null;

        firstOrders = orderRepository.getFirstOrderPerCustomer();
        return firstOrders;
    }
}
