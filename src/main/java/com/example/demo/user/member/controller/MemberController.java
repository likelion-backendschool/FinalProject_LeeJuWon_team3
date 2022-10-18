package com.example.demo.user.member.controller;

import com.example.demo.post.entity.Post;
import com.example.demo.post.form.PostForm;
import com.example.demo.user.member.entity.SiteUser;
import com.example.demo.user.member.form.MemberCreateForm;
import com.example.demo.user.member.form.MemberModifyForm;
import com.example.demo.user.member.form.MemberModifyPasswordForm;
import com.example.demo.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원가입
     */
    @GetMapping("/join")
    public String join(MemberCreateForm memberCreateForm) {
        return "members/join_form";
    }

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public String join(
            @Valid MemberCreateForm memberCreateForm,
            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "members/join_form";
        }

        if(!memberCreateForm.getPassword().equals(memberCreateForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "members/join_form";
        }


        try {
            memberService.create(memberCreateForm.getUsername(),
                    memberCreateForm.getEmail(),
                    memberCreateForm.getPassword(),
                    memberCreateForm.getNickname());

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


    /**
     * 로그인
     */
    @GetMapping("/login")
    public String login() {
        return "members/login_form";
    }


    /**
     * 회원정보수정
     * email, nickname
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String infoModify(
            MemberModifyForm memberModifyForm,
            Principal principal) {

        SiteUser siteUser = memberService.getSiteUser(principal.getName());

        memberModifyForm.setUsername(siteUser.getUsername());
        memberModifyForm.setEmail(siteUser.getEmail());
        memberModifyForm.setNickname(siteUser.getNickname());

        return "members/modify_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String infoModify(
            @Valid MemberModifyForm memberModifyForm,
            BindingResult bindingResult,
            Principal principal) {

        if(bindingResult.hasErrors()) {
            return "members/modify_form";
        }

        SiteUser siteUser = memberService.getSiteUser(principal.getName());

        memberService.modify(siteUser,
                memberModifyForm.getEmail(),
                memberModifyForm.getNickname());

        return "redirect:/";
    }


    /**
     * 비밀번호변경
     * password
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String infoModifyPassword(
            MemberModifyPasswordForm memberModifyPasswordForm,
            Principal principal) {

        SiteUser siteUser = memberService.getSiteUser(principal.getName());
        memberModifyPasswordForm.setUsername(siteUser.getUsername());

        return "members/modify_password_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String infoModifyPassword(
            @Valid MemberModifyPasswordForm memberModifyPasswordForm,
            BindingResult bindingResult,
            Principal principal) {

        if(bindingResult.hasErrors()) {
            return "members/modify_password_form";
        }

        SiteUser siteUser = memberService.getSiteUser(principal.getName());

        boolean sameOldPassword = memberService.isSameOldPassword(siteUser, memberModifyPasswordForm);

        if(!sameOldPassword) {
            bindingResult.rejectValue("oldPassword", "passwordInCorrect",
                    "이전 패스워드가 일치하지 않습니다.");
            return "members/modify_password_form";
        }

        if(!memberModifyPasswordForm.getPassword().equals(memberModifyPasswordForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "members/modify_password_form";
        }

        memberService.modifyPassword(siteUser, memberModifyPasswordForm.getPassword());

        return "redirect:/";
    }


}
