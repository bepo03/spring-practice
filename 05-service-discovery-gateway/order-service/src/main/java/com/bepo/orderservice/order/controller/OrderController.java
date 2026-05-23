package com.bepo.orderservice.order.controller;

import com.bepo.orderservice.order.dto.OrderCreateRequest;
import com.bepo.orderservice.order.dto.OrderResponse;
import com.bepo.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(
        @RequestBody
        OrderCreateRequest request,
        @RequestHeader(value = "X-Trace-Id", required = false)
        String traceId
    ) {
        return orderService.create(request, traceId);
    }
}
