package com.bepo.kafkaspringdockerui.order.controller;

import com.bepo.kafkaspringdockerui.order.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProducer producer;

    @PostMapping
    public String createOrder(
            @RequestParam(required = false) String userId,
            @RequestParam String message
    ) {
        producer.sendOrder(userId, message);
        return "주문 전송됨: " + message;
    }
}
