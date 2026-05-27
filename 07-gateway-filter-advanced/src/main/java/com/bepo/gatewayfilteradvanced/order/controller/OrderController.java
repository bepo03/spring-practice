package com.bepo.gatewayfilteradvanced.order.controller;

import com.bepo.gatewayfilteradvanced.order.dto.OrderResponse;
import com.bepo.gatewayfilteradvanced.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(
            @PathVariable
            Long id,
            @RequestHeader(value = "X-Trace-Id", required = false)
            String traceId
    ) {
        OrderResponse response = orderService.findById(id, traceId);

        return ResponseEntity.ok(response);
    }
}
