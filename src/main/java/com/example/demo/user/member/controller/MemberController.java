package com.example.demo.user.member.controller;

import com.example.demo.user.member.form.MemberCreateForm;
import com.example.demo.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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


        try {
            memberService.create(memberCreateForm.getUsername(),
                    memberCreateForm.getEmail(),
                    memberCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("joinFailed", "이미 등록된 사용자입니다.");
            return "members/join_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("joinFailed", e.getMessage());
            return "members/join_form";
        }


        return "redirect:/";
    }


    @GetMapping("/login")
    public String login() {
        return "members/login_form";
    }

}
