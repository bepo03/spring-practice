package com.bepo.paymentservice.payment.service;

import com.bepo.paymentservice.payment.dto.PaymentResponse;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {

    public PaymentResponse pay() {
        boolean failed = ThreadLocalRandom.current().nextBoolean();

        if (failed) {
            throw new IllegalStateException("결제 서비스 호출 실패");
        }

        return new PaymentResponse("PAID", "결제가 완료되었습니다.");
    }
}
