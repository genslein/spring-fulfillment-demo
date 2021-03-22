package com.demo.fulfillment.services;

import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public List<Pair<String, Order>> getEachCustomerFirstOrder() {
        List<Pair<String, Order>> firstOrders = null;

        return firstOrders;
    }
}
