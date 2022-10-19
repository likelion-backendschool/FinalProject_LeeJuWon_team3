package com.example.demo.ebook.service;

import com.example.demo.ebook.entity.Product;
import com.example.demo.ebook.repository.ProductRepository;
import com.example.demo.post.entity.Post;
import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.repository.MemberRepository;
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
    public void create(String subject, int price, Member member) {
        Product product = new Product();
        product.setSubject(subject);
        product.setPrice(price);
        product.setAuthor(member);
        product.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}
