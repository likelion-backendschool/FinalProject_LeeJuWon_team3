package com.project.Week_Mission.app.cart.dto;

import com.project.Week_Mission.app.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter @Setter
public class CartItemDto {

    private Long id;
    private Long memberId;
    private Long productId;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;


    public CartItemDto(CartItem cartItem) {
        id = cartItem.getId();
        memberId = cartItem.getMember().getId();
        productId = cartItem.getProduct().getId();
        createDate = cartItem.getCreateDate();
        modifyDate = cartItem.getModifyDate();
    }
}
