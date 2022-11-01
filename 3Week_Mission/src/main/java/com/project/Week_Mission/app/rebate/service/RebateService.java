package com.project.Week_Mission.app.rebate.service;

import com.project.Week_Mission.app.order.entity.OrderItem;
import com.project.Week_Mission.app.order.service.OrderService;
import com.project.Week_Mission.app.rebate.entity.RebateOrderItem;
import com.project.Week_Mission.app.rebate.repository.RebateOrderItemRepository;
import com.project.Week_Mission.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {

    private final RebateOrderItemRepository rebateOrderItemRepository;
    private final OrderService orderService;

    public void makeDate(String yearMonth) {

        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);
        LocalDateTime toDate = Ut.date.parse(toDateStr);

        // 데이터 가져오기
        List<OrderItem> orderItems = orderService.findAllByPayDateBetween(fromDate, toDate);

        // 변환하기
        List<RebateOrderItem> rebateOrderItems = orderItems
                .stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());

        // 저장하기
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    public void makeRebateOrderItem(RebateOrderItem item) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(item.getOrderItem().getId()).orElse(null);

        if (oldRebateOrderItem != null) {
            rebateOrderItemRepository.delete(oldRebateOrderItem);
        }

        rebateOrderItemRepository.save(item);
    }

    public RebateOrderItem toRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }
}
