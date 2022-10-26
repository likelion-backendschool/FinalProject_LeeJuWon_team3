package com.project.Week_Mission.app.cart.service;

import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.mybook.MyBook;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;


//  Test 용도
    public List<CartItem> findCartItems(Member member) {
        return cartRepository.findAllByMemberId(member.getId());
    }

//
//    public List<CartItemDto> findCartItemsByMemberDto(MemberDto memberDto) {
//
//        Product product = productRepository.findProductByAuthorId(memberDto.getId())
//                .orElseThrow(() -> new RuntimeException(memberDto.getId() + " memberDtoID has no product."));
//
//        List<CartItem> cartItemList = cartRepository.findCartItemListByMemberIdAndProductId(memberDto.getId(), product.getId());
//
//        List<CartItemDto> collect = cartItemList.stream()
//                .map(o -> new CartItemDto(o))
//                .collect(Collectors.toList());
//
//        return collect;
//    }


//    Test 용도
    @Transactional
    public CartItem addCartItem(Member member, Product product) {

        CartItem cartItem = CartItem.builder()
                .member(member)
                .product(product)
                .build();

        cartRepository.save(cartItem);

        return cartItem;
    }

//    @Transactional
//    public void add(MemberDto memberDto, ProductDto productDto) {
//
//        Long memberId = memberDto.getId();
//        Member member = memberRepository.findById(memberId).orElseThrow(
//                () -> new RuntimeException(memberId + " memberId is not found"));
//
//        Long productId = productDto.getId();
//        Product product = productRepository.findById(productId).orElseThrow(
//                () -> new RuntimeException(productId + " productId is not found"));
//
//
//        CartItem cartItem = CartItem.builder()
//                .member(member)
//                .product(product)
//                .build();
//
//        cartRepository.save(cartItem);
//    }
}
