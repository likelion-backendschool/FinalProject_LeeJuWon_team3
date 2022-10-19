package com.example.demo.ebook.repository;

import com.example.demo.ebook.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

//    List<Product> findByMemberId(long memberId);


}
