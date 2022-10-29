package com.project.Week_Mission.app.order.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.cart.dto.CartItemDto;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    private final Rq rq;

    private final MemberService memberService;

    /**
     * 주문생성
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Principal principal, Model model) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        Order order = orderService.createFromCart(memberDto);

        model.addAttribute("order", order);

        return "order/detail";
    }


    /**
     * 주문리스트
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, Principal principal) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        List<Order> orders = orderService.findOrdersByMemberDto(memberDto);

        model.addAttribute("orders", orders);

        return "order/list";
    }



    /**
     * 주문상세
     */



    /**
     * 주문취소
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{orderId}/cancel")
    public String cancel(@PathVariable long orderId) {

        orderService.cancelOrder(orderId);
        return "redirect:/order/list";
    }








    /**
     * 결제처리
     */



    /**
     * 환불처리
     */
}
