package com.bepo.kafkaspringdockerui.order.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOrder(String userId, String message) {
        kafkaTemplate.executeInTransaction(operations -> {
            operations.send("orders", userId, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        var meta = result.getRecordMetadata();
                        System.out.printf("트랜잭션 전송 성공! 파티션=%d, offset=%d\n", meta.partition(), meta.offset());
                    } else {
                        System.out.println("트랜잭션 전송 실패: " + ex.getMessage());
                    }
                });

            return null;
        });
    }
}
