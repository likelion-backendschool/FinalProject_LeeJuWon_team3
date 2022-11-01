package com.project.Week_Mission.app.product.dto;

import com.project.Week_Mission.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private Long authorId;
    private Long postKeywordId;
    private String subject;
    private int price;
    private int stockQuantity;

    private String authorName;
    private String jdentation;

    public ProductDto(Product product) {
        id = product.getId();
        authorId = product.getAuthor().getId();
        authorName = product.getAuthor().getName();
        jdentation = product.getAuthor().getJdenticon();
        postKeywordId = product.getPostKeyword().getId();
        subject = product.getSubject();
        price = product.getPrice();
        stockQuantity = product.getStockQuantity();
    }
}
