package com.project.Week_Mission.app.cart.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.cart.dto.CartItemDto;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ProductService productService;
    private final Rq rq;


    /**
     * 품목리스트 Test
     */
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/list")
//    public String list(Model model) {
//
//        Member member = rq.getMember();
//
//        List<CartItem> cartItems = cartService.findCartItems(member);
//
//        model.addAttribute("cartItems", cartItems);
//
//        return "cart/list";
//    }


    /**
     * 품목리스트
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, Principal principal) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        List<CartItemDto> cartItemDtos = cartService.findCartItemsByMemberDto(memberDto);

        model.addAttribute("cartItemDtos", cartItemDtos);

        return "cart/list";
    }



    /**
     * 품목삭제
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/remove/{productId}")
    public String remove(@PathVariable long productId, Principal principal) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        ProductDto productDto = productService.findByProductId(productId);

        cartService.removeCartItem(memberDto, productDto);

        return "redirect:/cart/list";
    }


    /**
     * 품목추가
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{productId}")
    public String addCartItem(CartItemDto cartItemDto,
                              @PathVariable long productId,
                              Model model) {

        ProductDto productDto = productService.findByProductId(productId);

        model.addAttribute("productDto", productDto);
        model.addAttribute("cartItemDto", cartItemDto);
        return "cart/create";
    }

    /**
     * 품목추가
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{productId}")
    public String addCartItem(@Valid CartItemDto cartItemDto,
                              @PathVariable long productId, Principal principal) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        ProductDto productDto = productService.findByProductId(productId);

        cartService.addCartItem(memberDto, productDto, cartItemDto.getQuantity());

        return "redirect:/product/list";
    }


    /**
     * 품목추가 Test
     */
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/add/{productId}")
//    public String addCartItem(@Valid CartItemDto cartItemDto,
//                              BindingResult bindingResult,
//                              @PathVariable long productId) {
//
//        if( bindingResult.hasErrors()) {
//            return "cart/create";
//        }
//
//        Member member = rq.getMember();
//        Product product = productService.findById(productId).orElseThrow(
//                () -> new RuntimeException(productId + " productId is not found."));
//
//        cartService.addCartItem(member, product, cartItemDto.getQuantity());
//
//        return "redirect:/cart/list";
//    }



}
