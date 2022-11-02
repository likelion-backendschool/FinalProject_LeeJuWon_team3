package com.project.Week_Mission.app.cash.service;

import com.project.Week_Mission.app.cash.entity.CashLog;
import com.project.Week_Mission.app.cash.repository.CashLogRepository;
import com.project.Week_Mission.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashLogService {

    private final CashLogRepository cashLogRepository;

    public CashLog addCash(Member member, long price, String eventType) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }

}
