package com.bepo.paymentservice.payment.dto;

public record PaymentResponse(
        String status,
        String message
) {
}
