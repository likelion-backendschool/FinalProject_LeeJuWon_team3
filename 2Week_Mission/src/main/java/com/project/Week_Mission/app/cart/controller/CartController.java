package com.project.Week_Mission.app.cart.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.cart.dto.CartItemDto;
import com.project.Week_Mission.app.cart.entity.CartItem;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model) {

        Member member = rq.getMember();

        List<CartItem> cartItems = cartService.findCartItems(member);

        model.addAttribute("cartItems", cartItems);

        return "cart/list";
    }


    /**
     * 품목리스트
     */
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/list")
//    public String list(String email, Model model) {
//
//        MemberDto memberDto = memberService.findByEmail(email).orElse(null);
//
//        if (memberDto == null) {
//            return rq.historyBack("일치하는 회원이 존재하지 않습니다.");
//        }
//
//        List<MyBookDto> myBooks = memberDto.getMyBookDtos();
//        List<CartItem> cartItems = cartService.findCartItemsByMyBook(myBooks);
//
////        List<CartItemDto> cartItemDtos = cartService.findCartItemsByMemberDto(memberDto);
//
//        model.addAttribute("cartItemDtos", cartItemDtos);
//
//        return "cart/list";
//    }



    /**
     * 품목삭제
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/remove/{productId}")
    public String remove(@PathVariable long productId) {

        Member member = rq.getMember();

        Product product = productService.findById(productId).orElseThrow(
                () -> new RuntimeException(productId + " productId is not found."));

        cartService.removeCartItem(member, product);

        return "redirect:/cart/list";
    }


    /**
     * 품목추가 Test
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/add/{productId}")
    public String addCartItem(CartItemDto cartItemDto,
                              @PathVariable long productId,
                              Model model) {

        Product product = productService.findById(productId).orElseThrow(
                () -> new RuntimeException(productId + " productId is not found."));

        model.addAttribute("product", product);
        model.addAttribute("cartItemDto", cartItemDto);
        return "cart/create";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{productId}")
    public String addCartItem(@Valid CartItemDto cartItemDto,
                              BindingResult bindingResult,
                              @PathVariable long productId) {

        if( bindingResult.hasErrors()) {
            return "cart/create";
        }

        Member member = rq.getMember();
        Product product = productService.findById(productId).orElseThrow(
                () -> new RuntimeException(productId + " productId is not found."));

        cartService.addCartItem(member, product, cartItemDto.getQuantity());

        return "redirect:/cart/list";
    }


    /**
     * 품목추가
     */
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/add/{productId}")
//    public String add(@PathVariable long id, String email) {
//
//        MemberDto memberDto = memberService.findByEmail(email).orElse(null);
//
//        if (memberDto == null) {
//            return rq.historyBack("일치하는 회원이 존재하지 않습니다.");
//        }
//
//        Optional<Product> productOptional = productService.findById(id);
//
//        ProductDto productDto = productOptional.map(o -> new ProductDto(o))
//                .orElseThrow(() -> new RuntimeException(productOptional + " product is not found"));
//
//        cartService.add(memberDto, productDto);
//
//        return "redirect:/cart/list";
//    }



}
