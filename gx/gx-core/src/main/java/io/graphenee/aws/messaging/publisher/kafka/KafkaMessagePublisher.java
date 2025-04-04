package io.graphenee.aws.messaging.publisher.kafka;

import io.graphenee.aws.messaging.Payload;
import io.graphenee.aws.messaging.publisher.MessagePublisher;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaMessagePublisher implements MessagePublisher {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaMessagePublisher(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String publish(String topic, Payload<?> payload) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send(topic, jsonPayload);
            return "Kafka message sent to topic: " + topic;
        } catch (Exception e) {
            throw new RuntimeException("Error serializing Kafka message", e);
        }
    }
}
