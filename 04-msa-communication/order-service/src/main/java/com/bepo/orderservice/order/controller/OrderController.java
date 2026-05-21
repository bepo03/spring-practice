package com.bepo.orderservice.order.controller;

import com.bepo.orderservice.member.client.MemberRestClient;
import com.bepo.orderservice.member.dto.MemberResponse;
import com.bepo.orderservice.order.dto.OrderCreateRequest;
import com.bepo.orderservice.order.dto.OrderResponse;
import com.bepo.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberRestClient memberRestClient;

    @PostMapping
    public OrderResponse createOrder(
            @RequestBody
            OrderCreateRequest request
    ) {
        return orderService.createOrder(request);
    }

    @GetMapping("/members/{memberId}/rest-client")
    public MemberResponse getMemberByRestClient(
            @PathVariable
            Long memberId
    ) {
        return memberRestClient.getMember(memberId);
    }
}
