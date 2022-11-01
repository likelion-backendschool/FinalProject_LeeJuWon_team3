package com.project.Week_Mission.app.order.service;

import com.project.Week_Mission.app.base.dto.RsData;
import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.mybook.service.MyBookService;
import com.project.Week_Mission.app.order.controller.OrderStatus;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.entity.OrderItem;
import com.project.Week_Mission.app.order.repository.OrderItemRepository;
import com.project.Week_Mission.app.order.repository.OrderRepository;
import com.project.Week_Mission.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    private final MyBookService myBookService;
    private final MemberService memberService;
    private final Rq rq;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createFromCart(MemberDto memberDto) {

        List<CartItem> cartItems = cartRepository.findAllByMemberId(memberDto.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();
            product.setQuantity(cartItem.getQuantity());

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

        Member member = memberRepository.findById(memberDto.getId()).orElseThrow(
                () -> new RuntimeException(memberDto.getId() + " is not found."));

        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.READY)
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
        order.setStatus(OrderStatus.CANCELED);
    }

    public List<Order> findOrdersByMemberDto(MemberDto memberDto) {
        return orderRepository.findOrderListByMemberId(memberDto.getId());
    }

    public Order findByOrderId(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException(orderId + "orderId is not found."));
    }

    @Transactional
    public RsData payByOrderId(long orderId) {

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException(orderId + " orderId is not found"));
        Member member = order.getMember();

        long restCash = member.getRestCash();

        int payPrice = order.calculatePayPrice();

        if(payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        //cashLog 엔터티는 예치금 사용할때 사용
        memberService.addCash(member, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));

        payDone(order);

        return RsData.of("S-1", "결제가 완료되었습니다.");
    }

    private void payDone(Order order) {
        order.setPaymentDone();
        order.setStatus(OrderStatus.COMPLETED);
        myBookService.add(order);
        orderRepository.save(order);
    }

    public boolean memberCanSee(Member member, Order order) {
        return member.getId().equals(order.getMember().getId());
    }

    public RsData actorCanCancel(Member actor, Order order) {
        if ( order.isPaid() ) {
            return RsData.of("F-3", "이미 결제처리 되었습니다.");
        }

        if (order.isCanceled()) {
            return RsData.of("F-1", "이미 취소되었습니다.");
        }

        if (actor.getId().equals(order.getMember().getId()) == false) {
            return RsData.of("F-2", "권한이 없습니다.");
        }

        return RsData.of("S-1", "취소할 수 있습니다.");
    }

    public RsData actorCanRefund(Member actor, Order order) {

        if (order.isCanceled()) {
            return RsData.of("F-1", "이미 취소되었습니다.");
        }

        if ( order.isRefunded() ) {
            return RsData.of("F-4", "이미 환불되었습니다.");
        }

        if ( order.isPaid() == false ) {
            return RsData.of("F-5", "결제가 되어야 환불이 가능합니다.");
        }

        if (actor.getId().equals(order.getMember().getId()) == false) {
            return RsData.of("F-2", "권한이 없습니다.");
        }

        long between = ChronoUnit.MINUTES.between(order.getPayDate(), LocalDateTime.now());

        if (between > 10) {
            return RsData.of("F-3", "결제 된지 10분이 지났으므로, 환불 할 수 없습니다.");
        }

        return RsData.of("S-1", "환불할 수 있습니다.");
    }

    public Order findForPrintById(long id) {
        return findById(id);
    }


    @Transactional
    public RsData payByTossPayments(Order order, long useRestCash) {

        Member member = order.getMember();
        long restCash = member.getRestCash();
        int payPrice = order.calculatePayPrice();

        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(member, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(member, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if (useRestCash > 0) {
            if (useRestCash > restCash) {
                throw new RuntimeException("예치금이 부족합니다.");
            }

            memberService.addCash(member, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));

        }

        payDone(order);

        return RsData.of("S-1", "결제가 완료되었습니다.");
    }

    @Transactional
    public RsData refund(Long orderId, Member member) {
        Order order = findById(orderId);

        if (order == null) {
            return RsData.of("F-2", "결제 상품을 찾을 수 없습니다.");
        }
        return refund(order, member);
    }


    @Transactional
    public RsData refund(Order order, Member actor) {
        RsData actorCanRefundRsData = actorCanRefund(actor, order);

        if (actorCanRefundRsData.isFail()) {
            return actorCanRefundRsData;
        }

        order.setCancelDone();

        int payPrice = order.getPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문__%d__환불__예치금".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);

        myBookService.remove(order);

        return RsData.of("S-1", "환불되었습니다.");
    }

    @Transactional
    public RsData payByRestCashOnly(Order order) {
        Member member = order.getMember();
        long restCash = member.getRestCash();
        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(member, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));

        payDone(order);

        return RsData.of("S-1", "결제가 완료되었습니다.");
    }

    public boolean memberCanPayment(Member member, Order order) {
        return memberCanSee(member, order);
    }

    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }
}
