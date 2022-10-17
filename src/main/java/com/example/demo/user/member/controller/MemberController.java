package com.example.demo.user.member.controller;

import com.example.demo.user.member.MemberCreateForm;
import com.example.demo.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/join")
    public String join(MemberCreateForm memberCreateForm) {
        return "members/join_form";
    }


    @PostMapping("/join")
    public String join(
            @Valid MemberCreateForm memberCreateForm,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "members/join_form";
        }

        if(!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "members/join_form";
        }


        memberService.create(memberCreateForm.getUsername(),
                memberCreateForm.getEmail(),
                memberCreateForm.getPassword1());

        return "redirect:/";
    }

}
