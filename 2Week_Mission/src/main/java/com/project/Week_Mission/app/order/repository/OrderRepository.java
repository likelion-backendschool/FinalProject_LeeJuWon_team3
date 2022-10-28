package com.project.Week_Mission.app.order.repository;

import com.project.Week_Mission.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
