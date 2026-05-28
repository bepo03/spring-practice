package com.bepo.orderservice.order.service;

import com.bepo.orderservice.order.client.PaymentClient;
import com.bepo.orderservice.order.dto.OrderResponse;
import com.bepo.orderservice.order.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final PaymentClient paymentClient;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "createOrderFallback")
    public OrderResponse createOrder() {
        PaymentResponse payment = paymentClient.pay();

        return new OrderResponse(
                "ORDER_CREATED",
                "주문이 생성되었습니다.",
                payment
        );
    }

    private OrderResponse createOrderFallback(Throwable throwable) {
        PaymentResponse payment = new PaymentResponse(
                "PAYMENT_PENDING",
                "결제 서비스 응답 실패로 결제 대기 상태입니다."
        );

        return new OrderResponse(
                "ORDER_PENDING",
                "주문은 접수되었고 결제는 대기 중입니다.",
                payment
        );
    }
}
