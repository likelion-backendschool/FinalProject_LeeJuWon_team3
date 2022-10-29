package com.project.Week_Mission.app.cart.dto;

import com.project.Week_Mission.app.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long id;
    private Long memberId;
    private Long productId;

    private String name;
    private int quantity;
    private int price;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;


    public CartItemDto(CartItem cartItem) {
        id = cartItem.getId();
        memberId = cartItem.getMember().getId();
        productId = cartItem.getProduct().getId();
        createDate = cartItem.getCreateDate();
        modifyDate = cartItem.getModifyDate();
        name = cartItem.getName();
        price = cartItem.getPrice();
        quantity = cartItem.getQuantity();
    }
}
