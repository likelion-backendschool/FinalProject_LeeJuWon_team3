package com.project.Week_Mission.app.order.entity;


import com.project.Week_Mission.app.base.entity.BaseEntity;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.order.controller.OrderStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();


    private LocalDateTime payDate; //결제날짜

    @Column(nullable = true)
    private boolean readyStatus; //주문완료여부

    @Column(nullable = true)
    private boolean isPaid; //결제완료여부

    @Column(nullable = true)
    private boolean isCanceled; //취소여부

    @Column(nullable = true)
    private boolean isRefunded; //환불여부

    private String name; //주문명

    private OrderStatus status;

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if(orderItems.size() > 1) {
            name += " 외 %d 상품".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }

    public Order(Member member) {
        this.member = member;
    }


    public int getPayPrice() {
        int payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }
}
