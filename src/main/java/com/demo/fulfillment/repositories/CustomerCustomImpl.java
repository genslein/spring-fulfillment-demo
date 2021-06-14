package com.demo.fulfillment.repositories;

import com.demo.fulfillment.models.Customer;
import com.demo.fulfillment.models.QCustomer;
import com.demo.fulfillment.models.QOrder;
import com.demo.fulfillment.models.subtypes.CustomerOrder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.ArrayList;
import java.util.List;

public class CustomerCustomImpl extends QuerydslRepositorySupport
 implements CustomerCustom {

    public CustomerCustomImpl() { super(Customer.class); }

    public List<CustomerOrder> getCustomerLatestOrders() {
        JPAQuery<Tuple> query = new JPAQuery<>(getEntityManager());

        List<Tuple> result = query.distinct().select(QCustomer.customer, QOrder.order)
                .from(QCustomer.customer)
                .join(QOrder.order)
                .on(QCustomer.customer.id.eq(QOrder.order.customerId))
                .orderBy(QOrder.order.createdAt.desc())
                .fetch();

        List<CustomerOrder> resultList = new ArrayList<>();
        for (Tuple t : result) {
            if (t.get(QCustomer.customer) != null && t.get(QOrder.order) != null) {
                resultList.add(CustomerOrder.builder()
                        .customer(t.get(QCustomer.customer))
                        .order(t.get(QOrder.order)).build());
            }
        }

        return resultList;
    }
}
