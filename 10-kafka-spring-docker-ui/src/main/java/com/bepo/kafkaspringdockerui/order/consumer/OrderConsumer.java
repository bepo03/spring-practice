package com.bepo.kafkaspringdockerui.order.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

// @Service
public class OrderConsumer {

    @KafkaListener(topics = "orders", groupId = "order-group")
    public void consume(
            ConsumerRecord<String, String> record,
            Acknowledgment ack
    ) {
        System.out.printf("받음 -> Key=%s, 파티션=%d, offset=%d, 내용=%s%n",
                record.key(), record.partition(), record.offset(), record.value());

        try {
            processOrder(record.value());
            ack.acknowledge();
        } catch (Exception e) {
            throw e;
        }
    }

    private void processOrder(String message) {
        System.out.println("주문 처리 완료: " + message);
    }
}
