package com.smartdelivery.orderservice.repository;

import com.smartdelivery.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
