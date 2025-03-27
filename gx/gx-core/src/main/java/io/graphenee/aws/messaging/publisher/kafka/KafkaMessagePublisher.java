package io.graphenee.aws.messaging.publisher.kafka;

import java.util.HashMap;
import java.util.Map;

import io.graphenee.aws.messaging.publisher.MessagePublisher;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaMessagePublisher implements MessagePublisher {

    private final KafkaProducer<String, String> kafkaProducer;
    private final ObjectMapper objectMapper;

    public KafkaMessagePublisher(String bootstrapServers, Boolean useAwsMskBootstrapServers) {
        this.objectMapper = new ObjectMapper();

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // IAM Authentication for AWS MSK
        if (useAwsMskBootstrapServers) {
            props.put("security.protocol", "SASL_SSL");
            props.put("sasl.mechanism", "AWS_MSK_IAM");
            props.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required;");
            props.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
        }

        this.kafkaProducer = new KafkaProducer<>(props);
    }

    @Override
    public String publish(String topic, String payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            kafkaProducer.send(new ProducerRecord<>(topic, message));
            return "Kafka message sent successfully to topic: " + topic;
        } catch (Exception e) {
            throw new RuntimeException("Error publishing to Kafka", e);
        }
    }
}
