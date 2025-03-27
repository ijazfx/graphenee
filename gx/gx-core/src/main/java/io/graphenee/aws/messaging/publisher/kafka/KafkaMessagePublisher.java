package io.graphenee.aws.messaging.publisher.kafka;

import io.graphenee.aws.messaging.Payload;
import io.graphenee.aws.messaging.publisher.MessagePublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class KafkaMessagePublisher implements MessagePublisher {

    private final KafkaTemplate<Object, Object> kafkaTemplate;


    public KafkaMessagePublisher(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String publish(String topic, Payload<?> payload) {
        Message<? extends Payload<?>> message = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.REPLY_TOPIC, "your-reply-topic") // ✅ Kafka only
                .build();

        kafkaTemplate.send(message);
        return "Kafka message sent to topic: " + topic;
    }
}

