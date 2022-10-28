package com.project.Week_Mission.app.order.entity;

import com.project.Week_Mission.app.base.entity.BaseEntity;
import com.project.Week_Mission.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderItem extends BaseEntity {


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    private LocalDateTime payDate; //결제날짜


    private int price; //가격

    private int salePrice; //실제판매가

    private int wholesalePrice; //도매가

    private int pgFee; //결제대행사 수수료

    private int payPrice; //결제금액

    private int refundPrice; //환불금액

    private boolean isPaid; //결제여부


    public OrderItem(Product product) {

    }

}
