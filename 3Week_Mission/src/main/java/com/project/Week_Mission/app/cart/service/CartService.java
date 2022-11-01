package com.project.Week_Mission.app.cart.service;

import com.project.Week_Mission.app.cart.dto.CartItemDto;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.product.dto.ProductDto;
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
    private final MemberRepository memberRepository;


//  Test 용도
//    public List<CartItem> findCartItems(Member member) {
//        return cartRepository.findAllByMemberId(member.getId());
//    }

    public List<CartItemDto> findCartItemsByMemberDto(MemberDto memberDto) {
        List<CartItem> cartItems = cartRepository.findAllByMemberId(memberDto.getId());

        List<CartItemDto> cartItemDtos = cartItems
                .stream()
                .map(o -> new CartItemDto(o))
                .collect(Collectors.toList());

        return cartItemDtos;
    }


//    Test 용도
//    @Transactional
//    public CartItem addCartItem(Member member, Product product, int quantity) {
//
//        CartItem oldCartItem = cartRepository.findCartItemByMemberIdAndProductId(member.getId(), product.getId()).orElse(null);
//
//        if (oldCartItem != null ) {
//            oldCartItem.setQuantity(oldCartItem.getQuantity() + quantity);
//            cartRepository.save(oldCartItem);
//
//            return oldCartItem;
//        }
//
//        CartItem cartItem = CartItem.builder()
//                .member(member)
//                .name(product.getSubject())
//                .product(product)
//                .price(product.getPrice())
//                .quantity(quantity)
//                .build();
//
//        cartRepository.save(cartItem);
//
//        return cartItem;
//    }

    @Transactional
    public CartItemDto addCartItem(MemberDto memberDto, ProductDto productDto, int quantity) {

        CartItem oldCartItem = cartRepository.findCartItemByMemberIdAndProductId(memberDto.getId(), productDto.getId()).orElse(null);

        if (oldCartItem != null ) {
            oldCartItem.setQuantity(oldCartItem.getQuantity() + quantity);
            cartRepository.save(oldCartItem);

            return new CartItemDto(oldCartItem);
        }

        Member member = memberRepository.findById(memberDto.getId()).orElseThrow(
                () -> new RuntimeException(memberDto.getId() + " memberId is not found"));

        Product product = productRepository.findById(productDto.getId()).orElseThrow(
                () -> new RuntimeException(productDto.getId() + " productId is not found"));


        CartItem cartItem = CartItem.builder()
                .member(member)
                .name(product.getSubject())
                .product(product)
                .price(product.getPrice())
                .quantity(quantity)
                .build();

        cartRepository.save(cartItem);

       return new CartItemDto(cartItem);
    }


    @Transactional
    public void removeCartItem(MemberDto memberDto, ProductDto productDto) {

        CartItem cartItem = cartRepository.findCartItemByMemberIdAndProductId(memberDto.getId(), productDto.getId())
                .orElseThrow(() -> new RuntimeException("cartItem is not found"));

        cartRepository.delete(cartItem);
    }

    @Transactional
    public void removeCartItem(CartItem cartItem) {
        cartRepository.delete(cartItem);
    }





}