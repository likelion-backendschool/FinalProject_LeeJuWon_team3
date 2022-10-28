package com.project.Week_Mission.app.order.entity;


import com.project.Week_Mission.app.base.entity.BaseEntity;
import com.project.Week_Mission.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();


    private LocalDateTime payDate; //결제날짜

    private boolean readyStatus; //주문완료여부

    private boolean isPaid; //결제완료여부

    private boolean isCanceled; //취소여부

    private boolean isRefunded; //환불여부

    private String name; //주문명


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
}
