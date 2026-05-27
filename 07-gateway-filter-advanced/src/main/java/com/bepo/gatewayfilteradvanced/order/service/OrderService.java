package com.bepo.gatewayfilteradvanced.order.service;

import com.bepo.gatewayfilteradvanced.order.dto.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    public OrderResponse findById(Long id, String traceId) {
        return new OrderResponse(
                id,
                "internal-order-api",
                traceId == null ? "" : traceId
        );
    }
}
