package com.project.Week_Mission.app.cart.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ProductService productService;
    private final Rq rq;


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





    /**
     * 품목추가 Test
     */
//    Test
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add/{productId}")
    public String addCartItem(@PathVariable long id) {

        Member member = rq.getMember();
        Product product = productService.findById(id).orElseThrow(
                () -> new RuntimeException(id + " productId is not found."));

        cartService.addCartItem(member, product);

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
