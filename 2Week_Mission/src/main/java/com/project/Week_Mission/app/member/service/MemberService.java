package com.project.Week_Mission.app.member.service;

import com.project.Week_Mission.app.AppConfig;
import com.project.Week_Mission.app.base.dto.RsData;
import com.project.Week_Mission.app.cash.entity.CashLog;
import com.project.Week_Mission.app.cash.service.CashLogService;
import com.project.Week_Mission.app.email.service.EmailService;
import com.project.Week_Mission.app.emailVerification.service.EmailVerificationService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.exception.AlreadyJoinException;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.security.dto.MemberContext;
import com.project.Week_Mission.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final CashLogService cashLogService;

    @Transactional
    public Member join(String username, String password, String email, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            throw new AlreadyJoinException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        emailVerificationService.send(member);

        return member;
    }

    public MemberDto findByUsername(String username) {
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Optional<MemberDto> memberDto = optionalMember.map(o -> new MemberDto(o));

        return findByUsername(memberDto);
    }

    private MemberDto findByUsername(Optional<MemberDto> memberDto) {
        return memberDto.orElseThrow(() -> new RuntimeException(memberDto + " is not found"));
    }

    @Transactional
    public RsData verifyEmail(long id, String verificationCode) {
        RsData verifyVerificationCodeRs = emailVerificationService.verifyVerificationCode(id, verificationCode);

        if (verifyVerificationCodeRs.isSuccess() == false) {
            return verifyVerificationCodeRs;
        }

        Member member = memberRepository.findById(id).get();
        member.setEmailVerified(true);

        return RsData.of("S-1", "이메일인증이 완료되었습니다.");
    }

    public Optional<Member> findByUsernameAndEmail(String username, String email) {
        return memberRepository.findByUsernameAndEmail(username, email);
    }

    @Transactional
    public RsData sendTempPasswordToEmail(Member actor) {
        String title = "[" + AppConfig.getSiteName() + "] 임시 패스워드 발송";
        String tempPassword = Ut.getTempPassword(6);
        String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
        body += "<a href=\"" + AppConfig.getSiteBaseUrl() + "/member/login\" target=\"_blank\">로그인 하러가기</a>";

        RsData sendResultData = emailService.sendEmail(actor.getEmail(), title, body);

        if (sendResultData.isFail()) {
            return sendResultData;
        }

        setTempPassword(actor, tempPassword);

        return RsData.of("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
    }

    @Transactional
    public void setTempPassword(Member actor, String tempPassword) {
        actor.setPassword(passwordEncoder.encode(tempPassword));
    }

    @Transactional
    public RsData modifyPassword(Member member, String password, String oldPassword) {
        Optional<Member> opMember = memberRepository.findById(member.getId());

        if (passwordEncoder.matches(oldPassword, opMember.get().getPassword()) == false) {
            return RsData.of("F-1", "기존 비밀번호가 일치하지 않습니다.");
        }

        opMember.get().setPassword(passwordEncoder.encode(password));

        return RsData.of("S-1", "비밀번호가 변경되었습니다.");
    }

    @Transactional
    public RsData beAuthor(Member member, String nickname) {
        Optional<Member> opMember = memberRepository.findByNickname(nickname);

        if (opMember.isPresent()) {
            return RsData.of("F-1", "해당 필명은 이미 사용중입니다.");
        }

        opMember = memberRepository.findById(member.getId());

        opMember.get().setNickname(nickname);
        forceAuthentication(opMember.get());

        return RsData.of("S-1", "해당 필명으로 활동을 시작합니다.");
    }

    private void forceAuthentication(Member member) {
        MemberContext memberContext = new MemberContext(member, member.genAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        memberContext,
                        member.getPassword(),
                        memberContext.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Transactional
    public void addCash(Member member, long price, String eventType) {

        CashLog cashLog = cashLogService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();

        member.setRestCash(newRestCash);

        // TODO save가 왜 필요한지. set으로 왜 안되는지 의문
        memberRepository.save(member);
    }

    public long getRestCash(Member member) {
        return memberRepository.findById(member.getId()).get().getRestCash();
    }

}
