package com.bepo.orderservice.order.dto;

public record OrderCreateRequest(
        Long memberId,
        String productName
) {
}
