package com.example.demo.ebook.controller;

import com.example.demo.ebook.entity.Product;
import com.example.demo.ebook.form.ProductForm;
import com.example.demo.ebook.service.ProductService;
import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
            productService.create(productForm.getSubject(), productForm.getPrice(), member);

        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("writeFailed", e.getMessage());
            return "products/product_form";
        }

        return "redirect:/usr/product/list"; //상품(도서)등록 후 상품(도서)목록으로 이동
    }

}
