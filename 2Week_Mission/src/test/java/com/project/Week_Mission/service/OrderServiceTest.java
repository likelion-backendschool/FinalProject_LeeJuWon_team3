package com.project.Week_Mission.service;

import com.project.Week_Mission.app.cart.dto.CartItemDto;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.entity.OrderItem;
import com.project.Week_Mission.app.order.repository.OrderRepository;
import com.project.Week_Mission.app.order.service.OrderService;
import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.entity.Product;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;



    @Test
    @DisplayName("장바구니 내역에서 주문 내역으로 이동")
    void t1() throws Exception {

        //given
        Member buyer1 = new Member();
        em.persist(buyer1);

        Product product1 = new Product();
        em.persist(product1);
        Product product2 = new Product();
        em.persist(product2);

        CartItem cartItem1 = new CartItem(buyer1, product1);
        em.persist(cartItem1);
        CartItem cartItem2 = new CartItem(buyer1, product2);
        em.persist(cartItem2);


        //when
        List<CartItem> cartItems1 = cartRepository.findAllByMemberId(buyer1.getId());
        int beforeRemovedCartItemSize = cartItems1.size();

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems1) {
            Product product = cartItem.getProduct();
            if (product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }
//            cartService.removeCartItem(cartItem);
            cartRepository.delete(cartItem);
        }

        List<CartItem> cartItems2 = cartRepository.findAllByMemberId(buyer1.getId());
        int afterRemovedCartItemSize = cartItems2.size();


        //then
        assertEquals(beforeRemovedCartItemSize, 2);
        assertEquals(afterRemovedCartItemSize, 0);
        assertThat(orderItems.size()).isEqualTo(2);
    }



//    @Test
//    @DisplayName("주문 내역 생성")
//    void t2() throws Exception {
//
//        //given
//        Member buyer1 = new Member();
//        buyer1.setUsername("곰돌이");
//        em.persist(buyer1);
//
//        PostKeyword postKeyword = new PostKeyword();
//
//        Product product1 = new Product(buyer1, postKeyword);
//        product1.setSubject("스프링1");
//        em.persist(product1);
//
//        //when
//        CartItemDto cartItemDto = cartService.addCartItem(new MemberDto(buyer1), new ProductDto(product1), 2);
//        Order order = orderService.createFromCart(buyer1);
//
//
//        //then
//        assertThat(order.getOrderItems().get(0).getOrder().getName()).isEqualTo(product1.getSubject());
//
//    }


}