package com.project.Week_Mission.app.cart.service;

import com.project.Week_Mission.app.cart.controller.CartItemDto;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public List<CartItemDto> findCartItemsByMemberDto(MemberDto memberDto) {

        Product product = productRepository.findProductByMemberId(memberDto.getId())
                .orElseThrow(() -> new RuntimeException(memberDto.getId() + " memberDtoID has no product."));

        List<CartItem> cartItemList = cartRepository.findCartItemListByMemberIdAndProductId(memberDto.getId(), product.getId());

        List<CartItemDto> collect = cartItemList.stream()
                .map(o -> new CartItemDto(o))
                .collect(Collectors.toList());

        return collect;
    }

}
