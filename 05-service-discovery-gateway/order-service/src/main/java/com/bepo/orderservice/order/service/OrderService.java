package com.bepo.orderservice.order.service;

import com.bepo.orderservice.member.client.MemberFeignClient;
import com.bepo.orderservice.member.dto.MemberResponse;
import com.bepo.orderservice.order.dto.OrderCreateRequest;
import com.bepo.orderservice.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberFeignClient memberFeignClient;

    public OrderResponse create(
            OrderCreateRequest request,
            String traceId
    ) {
        MemberResponse member = memberFeignClient.findById(request.memberId(), traceId);

        return new OrderResponse(
                1L,
                request.productId(),
                request.quantity(),
                member
        );
    }
}
