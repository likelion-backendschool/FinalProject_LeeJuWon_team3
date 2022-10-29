package com.project.Week_Mission.app.order.service;

import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.order.entity.OrderItem;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.repository.OrderRepository;
import com.project.Week_Mission.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final MemberRepository memberRepository;

    @Transactional
    public Order createFromCart(MemberDto memberDto) {

        List<CartItem> cartItems = cartRepository.findAllByMemberId(memberDto.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if(product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }
//            TODO 주문완료하면 장바구니를 비우는 것으로 수정하기
            cartService.removeCartItem(cartItem);
        }

        return create(memberDto, orderItems);
    }


    @Transactional
    public Order create(MemberDto memberDto, List<OrderItem> orderItems) {

        Member buyer = memberRepository.findById(memberDto.getId()).orElseThrow(
                () -> new RuntimeException(memberDto.getId() + " is not found."));

        Order order = Order.builder()
                .member(buyer)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.makeName();

//        TODO 결제를 하면 주문을 저장하도록 구현하기
        orderRepository.save(order);

        return order;
    }

    public Order findByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    public Order findById(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException(orderId + " orderId is not found."));
    }


    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(orderId + " orderId is not found."));

        order.setCanceled(true);
    }

    public List<Order> findOrdersByMemberDto(MemberDto memberDto) {
        return orderRepository.findOrderListByMemberId(memberDto.getId());
    }
}
