package com.project.Week_Mission.service;

import com.project.Week_Mission.app.cart.controller.CartItemDto;
import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.postTag.entity.PostTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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


    @Test
    @DisplayName("품목추가")
    void t1() {

        Member member = new Member();
        MemberDto memberDto = new MemberDto(member);
        List<CartItemDto> cartItemDtos = cartService.findCartItemsByMemberDto(memberDto);

    }


}