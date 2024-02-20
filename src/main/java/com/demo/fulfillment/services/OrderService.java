package com.demo.fulfillment.services;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.repositories.OrderRepository;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderRepository orderRepository;

    public List<Order> searchOrders(Predicate predicate) {
        return (List<Order>) orderRepository.findAll(predicate);
    }

    public List<Pair<Order, Customer>> getEachCustomerOrder() {
        List<Pair<Order, Customer>> firstOrders;

        firstOrders = orderRepository.getOrderCustomerRecords();
        logger.info("Found pairs of customer orders: " + firstOrders.size());
        return firstOrders;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
