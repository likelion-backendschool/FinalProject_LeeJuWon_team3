package com.project.Week_Mission.app.order.service;

import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.order.form.OrderForm;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;


    @Transactional
    public Order create(Member member, OrderForm orderForm) {

        Order order = Order.builder()
                .member(member)
                .payDate(orderForm.getPayDate()) //결제날짜
                .readyStatus(orderForm.isReadyStatus()) //주문완료여부
                .isPaid(orderForm.isPaid()) //결제완료여부
                .isCanceled(orderForm.isCanceled()) //취소여부
                .isRefunded(orderForm.isRefunded()) //환불여부
                .name(orderForm.getName()) //주문명
                .build();

        orderRepository.save(order);

        return order;
    }
}
