package com.bepo.kafkaspringdockerui.order.consumer;

import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumerWithRetry {

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "true",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR
    )
    @KafkaListener(topics = "orders", groupId = "order-group")
    public void consume(String message, Acknowledgment ack) {
        if (message.contains("FAIL")) {
            throw new RuntimeException("처리 실패!");
        }

        System.out.println("처리 완료: " + message);
        ack.acknowledge();
    }

    @DltHandler
    public void handleDlt(
            String message,
            @Header(KafkaHeaders.ORIGINAL_TOPIC) String originalTopic,
            @Header(KafkaHeaders.EXCEPTION_MESSAGE) String reason
    ) {
        System.out.printf("DLT 도착! 원래 토픽=%s, 이유=%s, 메시지=%s%n",
                originalTopic, reason, message);
    }
}
