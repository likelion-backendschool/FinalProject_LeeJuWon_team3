package com.project.Week_Mission.app.order.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.order.form.OrderForm;
import com.project.Week_Mission.app.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    private final Rq rq;

    /**
     * 주문생성
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(OrderForm orderForm) {
        return "order/create";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid OrderForm orderForm,
                         BindingResult bindingResult) {

        if( bindingResult.hasErrors()) {
            return "order/create";
        }

        Member member = rq.getMember();

        orderService.create(member, orderForm);

        return "order/list";
    }




    /**
     * 주문리스트
     */



    /**
     * 주문상세
     */



    /**
     * 주문취소
     */



    /**
     * 결제처리
     */



    /**
     * 환불처리
     */
}
