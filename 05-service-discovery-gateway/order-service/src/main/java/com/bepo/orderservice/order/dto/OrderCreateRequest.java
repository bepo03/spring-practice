package com.bepo.orderservice.order.dto;

public record OrderCreateRequest(
        Long memberId,
        Long productId,
        int quantity
) {
}
