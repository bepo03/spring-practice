package com.bepo.orderservice.order.dto;

public record OrderResponse(
        String status,
        String message,
        PaymentResponse payment
) {
}
