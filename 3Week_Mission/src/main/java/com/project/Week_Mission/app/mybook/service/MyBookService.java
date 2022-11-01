package com.project.Week_Mission.app.mybook.service;

import com.project.Week_Mission.app.base.dto.RsData;
import com.project.Week_Mission.app.mybook.entity.MyBook;
import com.project.Week_Mission.app.mybook.repository.MyBookRepository;
import com.project.Week_Mission.app.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {

    private final MyBookRepository myBookRepository;


    @Transactional
    public RsData add(Order order) {

        order.getOrderItems()
                .stream()
                .map(orderItem -> MyBook.builder()
                        .member(order.getMember())
                        .orderItem(orderItem)
                        .product(orderItem.getProduct())
                        .build())
                .forEach(myBookRepository::save);

        return RsData.of("S-1", "나의 책장에 추가되었습니다.");
    }


    @Transactional
    public RsData remove(Order order) {
        order.getOrderItems()
                .stream()
                .forEach(orderItem -> myBookRepository.deleteByProductIdAndMemberId(orderItem.getProduct().getId(), order.getMember().getId()));

        return RsData.of("S-1", "나의 책장에서 제거되었습니다.");
    }
}
