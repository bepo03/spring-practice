package com.bepo.gatewayfilteradvanced.order.dto;

public record OrderResponse(
        Long id,
        String service,
        String traceId
) {
}
