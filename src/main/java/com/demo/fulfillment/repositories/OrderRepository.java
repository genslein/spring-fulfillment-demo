package com.demo.fulfillment.repositories;

import com.demo.fulfillment.models.Order;
import com.demo.fulfillment.models.QOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID>,
        QuerydslBinderCustomizer<QOrder>,
        QuerydslPredicateExecutor<Order> {
}