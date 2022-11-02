package com.project.Week_Mission.app.product.entity;

import com.project.Week_Mission.app.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.*;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ProductOption extends BaseEntity {

    private int price;

    @ManyToOne(fetch = LAZY)
    private Product product;

    private boolean isSoldOut; // 사입처에서의 품절여부
    private int stockQuantity; // 쇼핑몰에서 보유한 물건 개수


}
