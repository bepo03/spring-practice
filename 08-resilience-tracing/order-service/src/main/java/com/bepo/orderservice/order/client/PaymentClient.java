package com.bepo.orderservice.order.client;

import com.bepo.orderservice.order.dto.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PaymentClient {

    private final RestClient restClient;

    public PaymentClient(
            @Value("${payment-service.url}")
            String paymentServiceUrl
    ) {
        this.restClient = RestClient.create(paymentServiceUrl);
    }

    public PaymentResponse pay() {
        return restClient.post()
                .uri("/api/payments")
                .retrieve()
                .body(PaymentResponse.class);
    }
}
