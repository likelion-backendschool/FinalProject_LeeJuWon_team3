package com.project.Week_Mission.app.product.dto;

import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;
import com.project.Week_Mission.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Getter @Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private Long authorId;
    private Long postKeywordId;
    private String subject;
    private int price;
    private int stockQuantity;


    public ProductDto(Product product) {
        id = product.getId();
        authorId = product.getAuthor().getId();
        postKeywordId = product.getPostKeyword().getId();
        subject = product.getSubject();
        price = product.getPrice();
        stockQuantity = product.getStockQuantity();
    }
}
