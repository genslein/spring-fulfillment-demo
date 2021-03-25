package com.demo.fulfillment.controllers;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.services.OrderService;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(name = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class Orders {

    @Autowired
    OrderService orderService;

    @GetMapping
    @ResponseBody
    List<Pair<Customer, Order>> getCustomerOrders() {
        return orderService.getEachCustomerFirstOrder();
    }

    @GetMapping
    @ResponseBody List<Order> searchOrders(@QuerydslPredicate Predicate predicate) {
        return orderService.searchOrders(predicate);
    }
}
