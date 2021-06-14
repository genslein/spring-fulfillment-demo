package com.demo.fulfillment.models.subtypes;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOrder {
    private Customer customer;
    private Order order;
}
