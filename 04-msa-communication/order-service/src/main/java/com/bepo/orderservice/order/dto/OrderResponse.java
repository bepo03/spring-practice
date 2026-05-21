package com.bepo.orderservice.order.dto;

public record OrderResponse(
        Long orderId,
        Long memberId,
        String memberName,
        String productName,
        String status
) {
}
