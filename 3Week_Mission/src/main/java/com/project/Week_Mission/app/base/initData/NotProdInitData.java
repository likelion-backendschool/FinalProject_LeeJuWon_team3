package com.project.Week_Mission.app.base.initData;

import com.project.Week_Mission.app.cart.repository.CartRepository;
import com.project.Week_Mission.app.cart.service.CartService;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.repository.OrderRepository;
import com.project.Week_Mission.app.order.service.OrderService;
import com.project.Week_Mission.app.post.service.PostService;
import com.project.Week_Mission.app.product.dto.ProductDto;
import com.project.Week_Mission.app.product.entity.Product;
import com.project.Week_Mission.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProdInitData {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService,
            OrderRepository orderRepository,
            CartRepository cartRepository
    ) {
        return args -> {
            if (initDataDone) {
                return;
            }

            initDataDone = true;


            Member member1 = memberService.join("user1", "1234", "user1@test.com", null);
            Member member2 = memberService.join("user2", "1234", "user2@test.com", "홍길순");

            postService.write(
                    member1,
                    "자바를 우아하게 사용하는 방법",
                    "# 내용 1",
                    "<h1>내용 1</h1>",
                    "#IT #자바 #카프카"
            );

            postService.write(
                    member1,
                    "자바스크립트를 우아하게 사용하는 방법",
                    """
                            # 자바스크립트는 이렇게 쓰세요.
                                                    
                            ```js
                            const a = 10;
                            console.log(a);
                            ```
                            """.stripIndent(),
                    """
                            <h1>자바스크립트는 이렇게 쓰세요.</h1><div data-language="js" class="toastui-editor-ww-code-block-highlighting"><pre class="language-js"><code data-language="js" class="language-js"><span class="token keyword">const</span> a <span class="token operator">=</span> <span class="token number">10</span><span class="token punctuation">;</span>
                            <span class="token console class-name">console</span><span class="token punctuation">.</span><span class="token method function property-access">log</span><span class="token punctuation">(</span>a<span class="token punctuation">)</span><span class="token punctuation">;</span></code></pre></div>
                                                    """.stripIndent(),
                    "#IT #프론트엔드 #리액트"
            );

            postService.write(member2, "제목 3", "내용 3", "내용 3", "#IT# 프론트엔드 #HTML #CSS");
            postService.write(member2, "제목 4", "내용 4", "내용 4", "#IT #스프링부트 #자바");
            postService.write(member1, "제목 5", "내용 5", "내용 5", "#IT #자바 #카프카");
            postService.write(member1, "제목 6", "내용 6", "내용 6", "#IT #프론트엔드 #REACT");
            postService.write(member2, "제목 7", "내용 7", "내용 7", "#IT# 프론트엔드 #HTML #CSS");
            postService.write(member2, "제목 8", "내용 8", "내용 8", "#IT #스프링부트 #자바");

            Product product1 = productService.create(member1, "상품명1", 30_000, "카프카", "#IT #카프카");
            Product product2 = productService.create(member2, "상품명2", 40_000, "스프링부트", "#IT #REACT");
            Product product3 = productService.create(member1, "상품명3", 50_000, "REACT", "#IT #REACT");
            Product product4 = productService.create(member2, "상품명4", 60_000, "HTML", "#IT #HTML");

            memberService.addCash(member1, 1_000_000, "충전__무통장입금");
            memberService.addCash(member1, 2_000_000, "충전__무통장입금");

            memberService.addCash(member2, 2_000_000, "충전__무통장입금");


            MemberDto memberDto1 = new MemberDto(member1);
            ProductDto productDto1 = new ProductDto(product1);
            cartService.addCartItem(memberDto1, productDto1, 1);
            cartService.addCartItem(memberDto1, productDto1, 2);
            cartService.addCartItem(memberDto1, productDto1, 1);

            Order order1 = orderService.createFromCart(memberDto1);

            int order1PayPrice = order1.calculatePayPrice();
            orderService.payByRestCashOnly(order1);

            ProductDto productDto2 = new ProductDto(product2);
            ProductDto productDto3 = new ProductDto(product3);
            ProductDto productDto4 = new ProductDto(product4);
            cartService.addCartItem(memberDto1, productDto2, 4);
            cartService.addCartItem(memberDto1, productDto3, 5);
            cartService.addCartItem(memberDto1, productDto4, 3);


//            orderService.refund(order1);


            MemberDto memberDto2 = new MemberDto(member2);
            cartService.addCartItem(memberDto2, productDto1, 3);
            cartService.addCartItem(memberDto2, productDto1, 6);
            cartService.addCartItem(memberDto2, productDto1, 4);

            Order order2 = orderService.createFromCart(memberDto2);

            int order2PayPrice = order2.calculatePayPrice();
            orderService.payByRestCashOnly(order2);
            cartService.addCartItem(memberDto2, productDto2, 2);
            cartService.addCartItem(memberDto2, productDto3, 7);
            cartService.addCartItem(memberDto2, productDto4, 1);

        };
    }
}
