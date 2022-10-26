package com.project.Week_Mission.service;

import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CartServiceTests {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    EntityManager em;

//    @Test
//    @DisplayName("품목추가")
//    void t1() throws Exception {
//
//        //given
//        Member member = new Member();
//        member.setUsername("홍길동");
//        em.persist(member);
//
//        Product product = new Product();
//        product.setSubject("책1");
//        em.persist(product);
//
//        //when
//        CartItem cartItem = cartService.addCartItem(member, product);
//        em.persist(cartItem);
//
//        //then
//        List<CartItem> cartItems = cartRepository.findCartItemListByMemberIdAndProductId(member.getId(), product.getId());
//        assertEquals(cartItems.size(), 1);
//
//        assertThat(cartItem.getMember().getUsername()).isEqualTo(member.getUsername());
//        assertThat(cartItem.getProduct().getSubject()).isEqualTo(product.getSubject());
//
//    }


    @Test
    @DisplayName("품목추가 및 품목리스트")
    void t2() throws Exception {

        //given
        Member member = new Member();
        member.setUsername("홍길동");
        em.persist(member);

        Product product = new Product();
        product.setSubject("책1");
        em.persist(product);

        Product product2 = new Product();
        product2.setSubject("책2");
        em.persist(product2);

        //when
        CartItem cartItem = cartService.addCartItem(member, product);
        em.persist(cartItem);
        CartItem cartItem2 = cartService.addCartItem(member, product2);
        em.persist(cartItem2);


        List<CartItem> cartItems = cartRepository.findAllByMemberId(member.getId());

        //then
        assertEquals(cartItems.size(), 2);
    }




}