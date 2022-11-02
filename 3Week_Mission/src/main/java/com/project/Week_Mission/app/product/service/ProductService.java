package com.project.Week_Mission.app.product.service;

import com.project.Week_Mission.app.member.entity.Member;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public Product create(Member author, String subject, int price, long postKeywordId, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findById(postKeywordId).get();

        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public Product create(Member author, String subject, int price, String postKeywordContent, String productTagContents) {
        PostKeyword postKeyword = postKeywordService.findByContentOrSave(postKeywordContent);

        return create(author, subject, price, postKeyword, productTagContents);
    }

    @Transactional
    public Product create(Member author, String subject, int price, PostKeyword postKeyword, String productTagContents) {
        Product product = Product
                .builder()
                .author(author)
                .postKeyword(postKeyword)
                .subject(subject)
                .price(price)
                .build();

        productRepository.save(product);

        applyProductTags(product, productTagContents);

        return product;
    }

    @Transactional
    public void applyProductTags(Product product, String productTagContents) {
        productTagService.applyProductTags(product, productTagContents);
    }

    public ProductDto findByProductId(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Optional<ProductDto> productDto = optionalProduct.map(o -> new ProductDto(o));
        return findByProductId(productDto);
    }

    private ProductDto findByProductId(Optional<ProductDto> productDto) {
        return productDto.orElseThrow(
                () -> new RuntimeException(productDto + " productDto is not found"));
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public void modify(Product product, String subject, int price, String productTagContents) {

        product.updateSubject(subject);
        product.updatePrice(price);

        applyProductTags(product, productTagContents);
    }

    public List<Product> findAllByOrderByIdDesc() {
        return productRepository.findAllByOrderByIdDesc();
    }

    public Optional<Product> findForPrintById(long id) {
        Optional<Product> opProduct = findById(id);

        if (opProduct.isEmpty()) return opProduct;

        List<ProductTag> productTags = getProductTags(opProduct.get());

        opProduct.get().getExtra().put("productTags", productTags);

        return opProduct;
    }

    private List<ProductTag> getProductTags(Product product) {
        return productTagService.getProductTags(product);
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

    public boolean actorCanModify(Member actor, Product product) {
        if (actor == null) return false;

        return actor.getId().equals(product.getAuthor().getId());
    }

    public boolean actorCanRemove(Member actor, Product product) {
        return actorCanModify(actor, product);
    }

    public List<Post> findPostsByProduct(Product product) {
        Member author = product.getAuthor();
        PostKeyword postKeyword = product.getPostKeyword();
        List<PostTag> postTags = postTagService.getPostTags(author.getId(), postKeyword.getId());

        return postTags
                .stream()
                .map(PostTag::getPost)
                .collect(Collectors.toList());
    }



    @Transactional
    public void remove(Product product) {
        productRepository.delete(product);
    }

    public List<Product> findAllForPrintByOrderByIdDesc(Member actor) {
        List<Product> products = findAllByOrderByIdDesc();

        loadForPrintData(products, actor);

        return products;
    }
}
