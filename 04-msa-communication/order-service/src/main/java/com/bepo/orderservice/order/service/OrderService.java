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

    public OrderResponse createOrder(OrderCreateRequest request) {
        MemberResponse member = memberFeignClient.getMember(request.memberId());

        return new OrderResponse(
                1L,
                member.id(),
                member.name(),
                request.productName(),
                "CREATED"
        );
    }
}
