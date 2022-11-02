package com.project.Week_Mission.app.product.dto;

import com.project.Week_Mission.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Long authorId;
    private Long postKeywordId;
    private String subject;
    private int price;
    private int stockQuantity;

    private String authorName;
    private String authorjdenticon;
    private String jdenticon;
    private String extra_productTagLinks;
    private String getExtra_inputValue_hashTagContents;
    private String postKeywordContent;

    public ProductDto(Product product) {
        id = product.getId();
        createDate = product.getCreateDate();
        modifyDate = product.getModifyDate();
        authorId = product.getAuthor().getId();
        authorName = product.getAuthor().getName();
        jdenticon = product.getJdenticon();
        authorjdenticon = product.getAuthor().getJdenticon();
        postKeywordId = product.getPostKeyword().getId();
        subject = product.getSubject();
        price = product.getPrice();
        stockQuantity = product.getStockQuantity();
        extra_productTagLinks = product.getExtra_productTagLinks();
        getExtra_inputValue_hashTagContents = product.getExtra_inputValue_hashTagContents();
        postKeywordContent = product.getPostKeyword().getContent();
    }
}
