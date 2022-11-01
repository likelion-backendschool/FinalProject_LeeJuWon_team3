package com.project.Week_Mission.app.rebate.repository;

import com.project.Week_Mission.app.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem, Long> {
    Optional<RebateOrderItem> findByOrderItemId(Long orderItemId);
}
