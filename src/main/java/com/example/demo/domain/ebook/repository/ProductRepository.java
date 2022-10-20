package com.example.demo.domain.ebook.repository;

import com.example.demo.domain.ebook.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    List<Product> findByMemberId(long memberId);


}
