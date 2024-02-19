package com.demo.fulfillment.repositories;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.models.QCustomer;
import com.demo.fulfillment.models.QOrder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class OrderCustomImpl extends QuerydslRepositorySupport
        implements OrderCustom {

    public OrderCustomImpl() {
        super(Order.class);
    }

    public List<Pair<Order, Customer>> getOrderCustomerRecords() {
        JPAQuery<Tuple> query = new JPAQuery<>();

        List<Tuple> result = query.select(QOrder.order, QCustomer.customer)
                .from(QOrder.order)
                .join(QCustomer.customer)
        .on(QCustomer.customer.id.eq(QOrder.order.customerId)).fetch();

        List<Pair<Order, Customer>> resultList = new ArrayList<>();
        for (Tuple t : result) {
            if (t.get(QCustomer.customer) != null && t.get(QOrder.order) != null) {
                resultList.add(Pair.of(t.get(QOrder.order), t.get(QCustomer.customer)));
            }
        }

        return resultList;
    }
}
