package com.project.Week_Mission.app.cash.entity;

import com.project.Week_Mission.app.base.entity.BaseEntity;
import com.project.Week_Mission.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private long price; //변동가격
    private String eventType; //변동종류(상품결제를 위한 충전, 상품결제, 상품환불로 인한 충전, 도서판매자로서 정산받음, 환전)

    public CashLog(long id) {
        super(id);
    }

}
