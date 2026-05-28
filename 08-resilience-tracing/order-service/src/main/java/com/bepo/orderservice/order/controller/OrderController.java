package com.bepo.orderservice.order.controller;

import com.bepo.orderservice.order.dto.OrderResponse;
import com.bepo.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder() {
        OrderResponse response = orderService.createOrder();

        return ResponseEntity.ok(response);
    }
}
