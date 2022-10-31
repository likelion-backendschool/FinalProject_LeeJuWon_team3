package com.project.Week_Mission.app.mybook.repository;

import com.project.Week_Mission.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    void deleteByProductIdAndMemberId(Long productId, Long memberId);
}
