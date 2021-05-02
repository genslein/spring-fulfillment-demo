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

public class OrderCustomDsl extends QuerydslRepositorySupport
        implements OrderCustom {

    public OrderCustomDsl() {
        super(Order.class);
    }

    public List<Pair<Customer, Order>> getFirstOrderPerCustomer() {
        JPAQuery<Tuple> query = new JPAQuery<>(getEntityManager());

        List<Tuple> result = query.join(QCustomer.customer)
        .on(QCustomer.customer.id.eq(QOrder.order.customerId)).fetch();

        List<Pair<Customer, Order>> resultList = new ArrayList<>();
        for (Tuple t : result) {
            if (t.get(QCustomer.customer) != null && t.get(QOrder.order) != null) {
                resultList.add(Pair.of(t.get(QCustomer.customer), t.get(QOrder.order)));
            }
        }

        return resultList;
    }
}
