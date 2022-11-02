package com.project.Week_Mission.app.product.service;

import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.repository.ProductRepository;
import com.project.Week_Mission.app.productTag.entity.ProductTag;
import com.project.Week_Mission.app.productTag.service.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductTagService productTagService;
    private final MemberRepository memberRepository;


    public List<ProductDto> findAllForPrintByOrderByIdDesc(Member actor) {
        List<Product> products = findAllByOrderByIdDesc();

        loadForPrintData(products, actor);

        return products.stream()
                .map(o -> new ProductDto(o))
                .collect(toList());
    }

    public List<Product> findAllByOrderByIdDesc() {
        return productRepository.findAllByOrderByIdDesc();
    }


    private void loadForPrintData(List<Product> products, Member actor) {
        long[] ids = products
                .stream()
                .mapToLong(Product::getId)
                .toArray();

        List<ProductTag> productTagsByProductIds = productTagService.getProductTagsByProductIdIn(ids);

        Map<Long, List<ProductTag>> productTagsByProductIdMap = productTagsByProductIds.stream()
                .collect(groupingBy(
                        productTag -> productTag.getProduct().getId(), toList()
                ));

        products.stream().forEach(product -> {
            List<ProductTag> productTags = productTagsByProductIdMap.get(product.getId());

            if (productTags == null || productTags.size() == 0) return;

            product.getExtra().put("productTags", productTags);
        });
    }


    public List<ProductTag> getProductTags(String productTagContent, Member actor) {
        List<ProductTag> productTags = productTagService.getProductTags(productTagContent);

        loadForPrintDataOnProductTagList(productTags, actor);

        return productTags;
    }


    private void loadForPrintDataOnProductTagList(List<ProductTag> productTags, Member actor) {
        List<Product> products = productTags
                .stream()
                .map(ProductTag::getProduct)
                .collect(toList());

        loadForPrintData(products, actor);
    }

    public boolean actorCanModify(Member actor, ProductDto productDto) {
        if (actor == null) return false;

        Product product = findProductByProductDtoId(productDto.getId());

        return actor.getId().equals(product.getAuthor().getId());
    }


    public boolean actorCanRemove(Member actor, ProductDto productDto) {
        return actorCanModify(actor, productDto);
    }


    public ProductDto findProductDtoByProductId(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " productId is not found."));

        return new ProductDto(product);
    }

    public Product findProductByProductDtoId(Long productDtoId) {
        return productRepository.findById(productDtoId)
                .orElseThrow(() -> new RuntimeException(productDtoId + " productDtoId is not found."));
    }


    public Product findProductByProductId(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(id + " productId is not found."));
    }


    public List<ProductTag> getProductTags(Product product) {
        return productTagService.getProductTags(product);
    }

    public Member findMemberByProductDto(ProductDto productDto) {
        return memberRepository.findById(productDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException(productDto.getAuthorId() + " productDto.getAuthorId() is not found."));
    }

}
