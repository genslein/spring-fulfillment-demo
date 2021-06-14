package com.demo.fulfillment.repositories;

import com.demo.fulfillment.models.subtypes.CustomerOrder;

import java.util.List;

public interface CustomerCustom {
    List<CustomerOrder> getCustomerLatestOrders();
}
