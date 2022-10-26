package com.project.Week_Mission.app.product.form;

import com.project.Week_Mission.app.product.entity.ProductOption;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductForm {
    @NotBlank
    private String subject;
    @NotNull
    private int price;
    @NotNull
    private Long postKeywordId;
    @NotBlank
    private String productTagContents;

    private List<ProductOption> options;
}
