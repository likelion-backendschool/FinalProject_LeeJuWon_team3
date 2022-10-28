package com.project.Week_Mission.app.order.repository;

import com.project.Week_Mission.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByMemberId(Long memberId);
}
