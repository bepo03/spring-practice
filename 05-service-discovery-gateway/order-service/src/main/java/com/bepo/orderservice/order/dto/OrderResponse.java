package com.bepo.orderservice.order.dto;

import com.bepo.orderservice.member.dto.MemberResponse;

public record OrderResponse(
        Long orderId,
        Long productId,
        int quantity,
        MemberResponse member
) {
}
