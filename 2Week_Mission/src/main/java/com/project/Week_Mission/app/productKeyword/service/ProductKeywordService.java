package com.project.Week_Mission.app.productKeyword.service;

import com.project.Week_Mission.app.productKeyword.entity.ProductKeyword;
import com.project.Week_Mission.app.productKeyword.repository.ProductKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductKeywordService {
    private final ProductKeywordRepository productKeywordRepository;

    public ProductKeyword save(String content) {
        Optional<ProductKeyword> optKeyword = productKeywordRepository.findByContent(content);

        if (optKeyword.isPresent()) {
            return optKeyword.get();
        }

        ProductKeyword productKeyword = ProductKeyword
                .builder()
                .content(content)
                .build();

        productKeywordRepository.save(productKeyword);

        return productKeyword;
    }

    public Optional<ProductKeyword> findByContent(String content) {
        return productKeywordRepository.findByContent(content);
    }

    public ProductKeyword findByContentOrSave(String content) {
        return save(content);
    }

    public Optional<ProductKeyword> findById(long id) {
        return productKeywordRepository.findById(id);
    }
}
