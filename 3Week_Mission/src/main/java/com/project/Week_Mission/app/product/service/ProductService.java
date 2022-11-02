package com.project.Week_Mission.app.product.service;

import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.repository.MemberRepository;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.postTag.entity.PostTag;
import com.project.Week_Mission.app.postTag.service.PostTagService;
import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;
import com.project.Week_Mission.app.postkeyword.service.PostKeywordService;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.repository.ProductRepository;
import com.project.Week_Mission.app.productTag.entity.ProductTag;
import com.project.Week_Mission.app.productTag.service.ProductTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final PostKeywordService postKeywordService;
    private final ProductTagService productTagService;
    private final PostTagService postTagService;
    private final ProductQueryService productQueryService;

    @Transactional
    public ProductDto create(Member author, String subject, int price, long postKeywordId, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findById(postKeywordId).get();

        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public ProductDto create(Member author, String subject, int price, String postKeywordContent, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findByContentOrSave(postKeywordContent);

        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public ProductDto create(Member author, String subject, int price, PostKeyword postKeyword, String productTagContents) {
        Product product = Product
                .builder()
                .author(author)
                .postKeyword(postKeyword)
                .subject(subject)
                .price(price)
                .build();

        productRepository.save(product);

        applyProductTags(product, productTagContents);

        return new ProductDto(product);
    }

    @Transactional
    public void modify(ProductDto productDto, String subject, int price, String productTagContents) {

        Product product = productQueryService.findProductByProductDtoId(productDto.getId());

        product.updateSubject(subject);
        product.updatePrice(price);

        applyProductTags(product, productTagContents);
    }

    @Transactional
    public void applyProductTags(Product product, String productTagContents) {
        productTagService.applyProductTags(product, productTagContents);
    }

    @Transactional
    public void remove(ProductDto productdto) {

        Product product = productQueryService.findProductByProductDtoId(productdto.getId());

        productRepository.delete(product);
    }

    public ProductDto findForPrintById(long id) {
        Product product = productQueryService.findProductByProductId(id);

        List<ProductTag> productTags = productQueryService.getProductTags(product);

        product.getExtra().put("productTags", productTags);

        return new ProductDto(product);
    }


    public List<Post> findPostsByProduct(ProductDto productDto) {

        Member author = productQueryService.findMemberByProductDto(productDto);
        Product product = productQueryService.findProductByProductDtoId(productDto.getId());

        PostKeyword postKeyword = product.getPostKeyword();
        List<PostTag> postTags = postTagService.getPostTags(author.getId(), postKeyword.getId());

        List<Post> list = new ArrayList<>();
        for (PostTag postTag : postTags) {
            Post post = postTag.getPost();
            list.add(post);
        }

        return list;
    }


}
