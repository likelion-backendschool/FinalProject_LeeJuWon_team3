package com.project.Week_Mission.app.order.service;

import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.order.entity.OrderItem;
import com.project.Week_Mission.app.order.form.OrderForm;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.repository.OrderRepository;
import com.project.Week_Mission.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @Transactional
    public Order createFromCart(Member buyer) {

        List<CartItem> cartItems = cartRepository.findAllByMemberId(buyer.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if(product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }
            cartService.removeCartItem(cartItem);
        }

        return create(buyer, orderItems);
    }


    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {

        Order order = Order.builder()
                .member(buyer)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.makeName();

        orderRepository.save(order);

        return order;
    }

    public Order findByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
