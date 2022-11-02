package com.project.Week_Mission.app.product.controller;

import com.project.Week_Mission.app.base.exception.ActorCanNotModifyException;
import com.project.Week_Mission.app.base.exception.ActorCanNotRemoveException;
import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;
import com.project.Week_Mission.app.postkeyword.service.PostKeywordService;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.form.ProductForm;
import com.project.Week_Mission.app.product.form.ProductModifyForm;
import com.project.Week_Mission.app.product.service.ProductQueryService;
import com.project.Week_Mission.app.product.service.ProductService;
import com.project.Week_Mission.app.productTag.entity.ProductTag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ProductQueryService productQueryService;
    private final PostKeywordService postKeywordService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @GetMapping("/create")
    public String showCreate(Model model) {
        List<PostKeyword> postKeywords = postKeywordService.findByMemberId(rq.getId());
        model.addAttribute("postKeywords", postKeywords);
        return "product/create";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('AUTHOR')")
    @PostMapping("/create")
    public String create(@Valid ProductForm productForm) {
        Member author = rq.getMember();
        ProductDto product = productService.create(author, productForm.getSubject(), productForm.getPrice(), productForm.getPostKeywordId(), productForm.getProductTagContents());
        return "redirect:/product/" + product.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ProductDto product = productService.findForPrintById(id);
        List<Post> posts = productService.findPostsByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("posts", posts);

        return "product/detail";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<ProductDto> products = productQueryService.findAllForPrintByOrderByIdDesc(rq.getMember());

        model.addAttribute("products", products);

        return "product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        ProductDto product = productService.findForPrintById(id);

        Member actor = rq.getMember();

        if (productQueryService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid ProductModifyForm productForm, @PathVariable long id) {
        ProductDto product = productQueryService.findProductDtoByProductId(id);
        Member actor = rq.getMember();

        if (productQueryService.actorCanModify(actor, product) == false) {
            throw new ActorCanNotModifyException();
        }

        productService.modify(product, productForm.getSubject(), productForm.getPrice(), productForm.getProductTagContents());
        return Rq.redirectWithMsg("/product/" + product.getId(), "%d번 도서 상품이 수정되었습니다.".formatted(product.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/remove")
    public String remove(@PathVariable long id) {
        ProductDto product = productQueryService.findProductDtoByProductId(id);
        Member actor = rq.getMember();

        if (productQueryService.actorCanRemove(actor, product) == false) {
            throw new ActorCanNotRemoveException();
        }

        productService.remove(product);

        return Rq.redirectWithMsg("/product/list", "%d번 상품이 삭제되었습니다.".formatted(product.getId()));
    }

    @GetMapping("/tag/{tagContent}")
    public String tagList(Model model, @PathVariable String tagContent) {
        List<ProductTag> productTags = productQueryService.getProductTags(tagContent, rq.getMember());

        model.addAttribute("productTags", productTags);
        return "product/tagList";
    }
}
