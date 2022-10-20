package com.example.demo.domain.ebook.service;

import com.example.demo.domain.ebook.entity.Product;
import com.example.demo.domain.ebook.repository.ProductRepository;
import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    private Member findMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Member " + username + "is not found."));
    }


    @Transactional
    public Product create(String subject, int price, Member member) {
        Product product = new Product();
        product.setSubject(subject);
        product.setPrice(price);
        product.setAuthor(member);
        product.setCreatedAt(LocalDateTime.now());
        Product result = productRepository.save(product);
        return result;
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product id" + id + "not found"));
    }

    @Transactional
    public void modify(Product product, String subject, int price) {
        product.setSubject(subject);
        product.setPrice(price);
    }


    @Transactional
    public void delete(Product product) {
        productRepository.delete(product);
    }
}
