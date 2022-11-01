package com.project.Week_Mission.app.order.form;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class OrderForm {

    private Long id;

    private String name; //주문명

    private LocalDateTime payDate; //결제날짜

    private boolean readyStatus; //주문완료여부

    private boolean isPaid; //결제완료여부

    private boolean isCanceled; //취소여부

    private boolean isRefunded; //환불여부



}
