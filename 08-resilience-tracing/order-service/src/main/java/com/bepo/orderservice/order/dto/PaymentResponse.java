package com.bepo.orderservice.order.dto;

public record PaymentResponse(
        String status,
        String message
) {
}
