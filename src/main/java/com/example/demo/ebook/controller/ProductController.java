package com.example.demo.ebook.controller;

import com.example.demo.ebook.entity.Product;
import com.example.demo.ebook.form.ProductForm;
import com.example.demo.ebook.service.ProductService;
import com.example.demo.post.entity.Post;
import com.example.demo.post.form.PostForm;
import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/usr")
public class ProductController {


    private final ProductService productService;
    private final MemberService memberService;

    /**
     * 도서목록
     * - 상품 리스트, 전체 노출
     * - 리스트 아이템 구성요소 : 번호, 상품명, 가격, 작성자, 작성날짜
     */
    @GetMapping("/product/list")
    public String productList(Model model) {

        List<Product> productList = productService.findProducts();
        model.addAttribute("productList", productList);

        return "products/product_list";
    }


    /**
     * 상품(도서) 등록
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/author/product/create")
    public String productCreate(ProductForm productForm) {
        return "products/product_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/author/product/create")
    public String productCreate(
            @Valid ProductForm productForm,
            BindingResult bindingResult,
            Principal principal) {

        if( bindingResult.hasErrors()) {
            return "products/product_form";
        }

        Member member = memberService.getMember(principal.getName());


        try {
            Product product = productService.create(productForm.getSubject(), productForm.getPrice(), member);
            Long id = product.getId();
            return String.format("redirect:/usr/author/product/%s", id); //상품(도서)등록 후 해당 상품(도서) 상세페이지로 이동

        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("writeFailed", e.getMessage());
            return "products/product_form";
        }

    }


    /**
     * 상품(도서) 상세
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/author/product/{id}")
    public String productDetail(
            Model model,
            @PathVariable("id") Long id) {

        Product product = productService.getProduct(id);

        model.addAttribute("product", product);

        return "products/product_detail";
    }


    /**
     * 상품(도서)수정
     */

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/author/product/{id}/modify")
    public String postModify(
            ProductForm productForm,
            @PathVariable("id") Long id,
            Principal principal) {
        Product product = productService.getProduct(id);


        if(!product.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        productForm.setSubject(product.getSubject());
        productForm.setPrice(product.getPrice());

        return "products/product_modify_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/author/product/{id}/modify")
    public String productModify(
            @Valid ProductForm productForm,
            BindingResult bindingResult,
            @PathVariable("id") Long id,
            Principal principal) {

        if(bindingResult.hasErrors()) {
            return "products/product_modify_form";
        }

        Product product = productService.getProduct(id);

        if(!product.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        try {
            productService.modify(product, productForm.getSubject(), productForm.getPrice());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("modifyFailed", e.getMessage());
            return "products/product_modify_form";
        }

        return String.format("redirect:/usr/product/%s", id);
    }



    /**
     * 상품(도서) 삭제
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/author/product/{id}/delete")
    public String productDelete(
            Principal principal,
            @PathVariable("id") Long id) {

        Product product = productService.getProduct(id);


        if (!product.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        productService.delete(product);

        return "redirect:/usr/product/list";
    }


}
