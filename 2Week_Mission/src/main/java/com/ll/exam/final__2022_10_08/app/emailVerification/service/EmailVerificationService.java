package com.ll.exam.final__2022_10_08.app.emailVerification.service;

import com.ll.exam.final__2022_10_08.app.AppConfig;
import com.ll.exam.final__2022_10_08.app.attr.service.AttrService;
import com.ll.exam.final__2022_10_08.app.base.dto.RsData;
import com.ll.exam.final__2022_10_08.app.email.service.EmailService;
import com.ll.exam.final__2022_10_08.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailService emailService;
    private final AttrService attrService;

    public RsData<Long> send(Member member) {
        String email = member.getEmail();
        String title = "[%s 이메일인증] 안녕하세요 %s님. 링크를 클릭하여 회원가입을 완료해주세요.".formatted(AppConfig.getSiteName(), member.getName());
        String url = genEmailVerificationUrl(member);

        RsData<Long> sendEmailRs = emailService.sendEmail(email, title, url);

        return sendEmailRs;
    }

    public String genEmailVerificationUrl(Member member) {
        return genEmailVerificationUrl(member.getId());
    }

    public String genEmailVerificationUrl(long memberId) {
        String code = genEmailVerificationCode(memberId);

        return AppConfig.getSiteBaseUrl() + "/emailVerification/verify?memberId=%d&code=%s".formatted(memberId, code);
    }

    public String genEmailVerificationCode(long memberId) {
        String code = UUID.randomUUID().toString();
        attrService.set("member__%d__extra__emailVerificationCode".formatted(memberId), code, LocalDateTime.now().plusSeconds(60 * 60 * 1));

        return code;
    }

    public RsData verifyVerificationCode(long memberId, String code) {
        String foundCode = attrService.get("member__%d__extra__emailVerificationCode".formatted(memberId), "");

        if (foundCode.equals(code) == false) {
            return RsData.of("F-1", "만료되었거나 유효하지 않은 코드입니다.");
        }

        return RsData.of("S-1", "인증된 코드 입니다.");
    }

    public String findEmailVerificationCode(long memberId) {
        return attrService.get("member__%d__extra__emailVerificationCode".formatted(memberId), "");
    }
}
