package com.project.Week_Mission.app.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Week_Mission.app.base.dto.RsData;
import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.member.service.MemberDto;
import com.project.Week_Mission.app.member.service.MemberService;
import com.project.Week_Mission.app.order.entity.Order;
import com.project.Week_Mission.app.order.service.OrderService;
import com.project.Week_Mission.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    private final Rq rq;

    private final MemberService memberService;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();



    /**
     * 주문생성
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Principal principal, Model model) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        Order order = orderService.createFromCart(memberDto);

        model.addAttribute("memberDto", memberDto);
        model.addAttribute("order", order);

        return "order/create";
    }


    /**
     * 주문리스트
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, Principal principal) {

        MemberDto memberDto = memberService.findByUsername(principal.getName());
        List<Order> orderList = orderService.findOrdersByMemberDto(memberDto);

        model.addAttribute("orderList", orderList);

        return "order/list";
    }


    /**
     * 주문상세 및 결제
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(Model model, @PathVariable long id) {

        Order order = orderService.findByOrderId(id);
        Member member = order.getMember();

        long restCash = member.getRestCash();

        if (orderService.memberCanSee(member, order) == false) {
            throw new ActorCanNotSeeOrderException();
        }

        model.addAttribute("order", order);
        model.addAttribute("actorRestCash", restCash);

        return "order/detail";
    }

    /**
     * 결제처리
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("{id}/pay")
    public String pay( @PathVariable long id) {

        Order order = orderService.findById(id);
        Member member = order.getMember();

        if (orderService.memberCanPayment(member, order) == false) {
            throw new ActorCanNotPayOrderException();
        }

        RsData rsData = orderService.payByOrderId(id);

        return "redirect:/order/list";
    }



    /**
     * 주문취소
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/cancel")
    public String cancel(@PathVariable long id) {

        orderService.cancelOrder(id);
        return "redirect:/order/list";
    }


    /**
     * 환불처리
     */
    @PostMapping("/{id}/refund")
    @PreAuthorize("isAuthenticated()")
    public String refund(@PathVariable Long id) {
        RsData rsData = orderService.refund(id, rq.getMember());

        if (rsData.isFail()) {
            return Rq.redirectWithErrorMsg("/order/%d".formatted(id), rsData);
        }

        return Rq.redirectWithMsg("/order/%d".formatted(id), rsData);
    }


    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    @Value("${custom.tossPayments.secretKey}")
    private String SECRET_KEY;

    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable long id,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model,
            @AuthenticationPrincipal MemberContext memberContext
    ) throws Exception {

        Order order = orderService.findForPrintById(id);

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if (id != orderIdInputed) {
            throw new OrderIdNotMatchedException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member actor = memberContext.getMember();
        long restCash = memberService.getRestCash(actor);
        long payPriceRestCash = order.calculatePayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new OrderNotEnoughRestCashException();
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            return Rq.redirectWithMsg(
                    "/order/%d".formatted(order.getId()),
                    "%d번 주문이 결제처리되었습니다.".formatted(order.getId())
            );
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }





}
