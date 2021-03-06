package com.demo.fulfillment.repositories;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import org.springframework.data.util.Pair;

import java.util.List;

public interface OrderCustom {
    List<Pair<Order, Customer>> getOrderCustomerRecords();
}
