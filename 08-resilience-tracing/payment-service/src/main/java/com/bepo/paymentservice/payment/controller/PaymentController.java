package com.bepo.paymentservice.payment.controller;

import com.bepo.paymentservice.payment.dto.PaymentResponse;
import com.bepo.paymentservice.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> pay() {
        PaymentResponse response = paymentService.pay();

        return ResponseEntity.ok(response);
    }
}
