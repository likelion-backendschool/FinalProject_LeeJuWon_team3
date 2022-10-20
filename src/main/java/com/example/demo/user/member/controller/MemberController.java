package com.example.demo.user.member.controller;

import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.form.MemberCreateForm;
import com.example.demo.user.member.form.MemberModifyForm;
import com.example.demo.user.member.form.MemberModifyPasswordForm;
import com.example.demo.user.member.form.MemberFindIdForm;
import com.example.demo.user.member.service.MemberService;
import com.example.demo.user.member.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final UserSecurityService userSecurityService;


    /**
     * 로그인
     */
    @GetMapping("/login")
    public String login() {
        return "members/login_form";
    }


    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }


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
     * 회원정보수정
     * email, nickname
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String infoModify(
            MemberModifyForm memberModifyForm,
            Principal principal) {

        Member member = memberService.getMember(principal.getName());

        memberModifyForm.setUsername(member.getUsername());
        memberModifyForm.setEmail(member.getEmail());
        memberModifyForm.setNickname(member.getNickname());

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

        Member member = memberService.getMember(principal.getName());

        memberService.modify(member,
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

        Member member = memberService.getMember(principal.getName());
        memberModifyPasswordForm.setUsername(member.getUsername());

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

        Member member = memberService.getMember(principal.getName());

        boolean sameOldPassword = memberService.isSameOldPassword(member, memberModifyPasswordForm);

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

        memberService.modifyPassword(member, memberModifyPasswordForm.getPassword());

        return "redirect:/";
    }




    /**
     * 아이디 찾기
     * TODO 오류 해결하기
     */
    @GetMapping("/findUsername")
    public String findId(
            MemberFindIdForm memberFindIdForm) {
        return "members/find_id_form";
    }

    @PostMapping("/findUsername")
    public String findId(@Valid MemberFindIdForm memberFindIdForm,
                         BindingResult bindingResult
    ) {

        if(bindingResult.hasErrors()) {
            return "members/find_id_form";
        }

        String email = memberFindIdForm.getEmail();
        Member member = memberService.findByEmail(email);


        String username = member.getUsername();
        System.out.println("username = " + username);

        return "redirect:/";
    }


}
