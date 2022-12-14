package com.project.Week_Mission.app.order.service;

import com.project.Week_Mission.app.base.dto.RsData;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.mybook.service.MyBookService;
import com.project.Week_Mission.app.order.status.OrderStatus;
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
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createFromCart(MemberDto memberDto) {

        List<CartItem> cartItems = cartRepository.findAllByMemberId(memberDto.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();
            product.updateQuantity(cartItem.getQuantity());

            if(product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }
//            TODO ?????????????????? ??????????????? ????????? ????????? ????????????
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

        int price = 0;
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
            price += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.updatePrice(price);
        order.makeName();

//        TODO ????????? ?????? ????????? ??????????????? ????????????
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

//        int payPrice = order.calculatePayPrice();
        int payPrice = order.getPrice();

        if(payPrice > restCash) {
            throw new RuntimeException("???????????? ???????????????.");
        }

        //cashLog ???????????? ????????? ???????????? ??????
        memberService.addCash(member, payPrice * -1, "??????__%d__??????__?????????".formatted(order.getId()));

        payDone(order);

        return RsData.of("S-1", "????????? ?????????????????????.");
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
            return RsData.of("F-3", "?????? ???????????? ???????????????.");
        }

        if (order.isCanceled()) {
            return RsData.of("F-1", "?????? ?????????????????????.");
        }

        if (actor.getId().equals(order.getMember().getId()) == false) {
            return RsData.of("F-2", "????????? ????????????.");
        }

        return RsData.of("S-1", "????????? ??? ????????????.");
    }

    public RsData actorCanRefund(Member actor, Order order) {

        if (order.isCanceled()) {
            return RsData.of("F-1", "?????? ?????????????????????.");
        }

        if ( order.isRefunded() ) {
            return RsData.of("F-4", "?????? ?????????????????????.");
        }

        if ( order.isPaid() == false ) {
            return RsData.of("F-5", "????????? ????????? ????????? ???????????????.");
        }

        if (actor.getId().equals(order.getMember().getId()) == false) {
            return RsData.of("F-2", "????????? ????????????.");
        }

        long between = ChronoUnit.MINUTES.between(order.getPayDate(), LocalDateTime.now());

        if (between > 10) {
            return RsData.of("F-3", "?????? ?????? 10?????? ???????????????, ?????? ??? ??? ????????????.");
        }

        return RsData.of("S-1", "????????? ??? ????????????.");
    }

    public Order findForPrintById(long id) {
        return findById(id);
    }


    @Transactional
    public RsData payByTossPayments(Order order, long useRestCash) {

        Member member = order.getMember();
        long restCash = member.getRestCash();
//        int payPrice = order.calculatePayPrice();
        int payPrice = order.getPrice();


        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(member, pgPayPrice, "??????__%d__??????__??????????????????".formatted(order.getId()));
        memberService.addCash(member, pgPayPrice * -1, "??????__%d__??????__??????????????????".formatted(order.getId()));

        if (useRestCash > 0) {
            if (useRestCash > restCash) {
                throw new RuntimeException("???????????? ???????????????.");
            }

            memberService.addCash(member, useRestCash * -1, "??????__%d__??????__?????????".formatted(order.getId()));

        }

        payDone(order);

        return RsData.of("S-1", "????????? ?????????????????????.");
    }

    @Transactional
    public RsData refund(Long orderId, Member member) {
        Order order = findById(orderId);

        if (order == null) {
            return RsData.of("F-2", "?????? ????????? ?????? ??? ????????????.");
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
        memberService.addCash(order.getMember(), payPrice, "??????__%d__??????__?????????".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);

        myBookService.remove(order);

        return RsData.of("S-1", "?????????????????????.");
    }

    @Transactional
    public RsData payByRestCashOnly(Order order) {
        Member member = order.getMember();
        long restCash = member.getRestCash();
//        int payPrice = order.calculatePayPrice();
        int payPrice = order.getPrice();


        if (payPrice > restCash) {
            throw new RuntimeException("???????????? ???????????????.");
        }

        memberService.addCash(member, payPrice * -1, "??????__%d__??????__?????????".formatted(order.getId()));

        payDone(order);

        return RsData.of("S-1", "????????? ?????????????????????.");
    }

    public boolean memberCanPayment(Member member, Order order) {
        return memberCanSee(member, order);
    }

    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }
}
