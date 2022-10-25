package com.project.Week_Mission.app.cart.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final Rq rq;


    /**
     * 품목리스트
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(String email, Model model) {

        MemberDto memberDto = memberService.findByEmail(email).orElse(null);

        if (memberDto == null) {
            return rq.historyBack("일치하는 회원이 존재하지 않습니다.");
        }

        List<CartItemDto> cartItemDtos = cartService.findCartItemsByMemberDto(memberDto);

        model.addAttribute("cartItemDtos", cartItemDtos);

        return "cart/list";
    }



    /**
     * 품목삭제
     */


    /**
     * 품목추가
     */
    public String cartAdd() {

    }
}
