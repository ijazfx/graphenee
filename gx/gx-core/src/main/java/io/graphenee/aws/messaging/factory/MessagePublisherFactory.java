package io.graphenee.aws.messaging.factory;

import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import io.graphenee.aws.messaging.publisher.MessagePublisher;
import io.graphenee.aws.messaging.publisher.kafka.KafkaMessagePublisher;
import io.graphenee.aws.messaging.publisher.sns.SnsMessagePublisher;

@Component
public class MessagePublisherFactory {

    private final Environment env;
    private final KafkaTemplate<Object, Object> kafkaTemplate; // Nullable for SNS

    public MessagePublisherFactory(Environment env) {
        this(env, null);
    }

    public MessagePublisherFactory(Environment env, KafkaTemplate<Object, Object> kafkaTemplate) {
        this.env = env;
        this.kafkaTemplate = kafkaTemplate;
    }

    public MessagePublisher createPublisher() {
        String provider = env.getProperty("messaging.provider", "sns").toLowerCase();

        switch (provider) {
            case "sns":
                return new SnsMessagePublisher(
                        getRequiredProperty("sns.accessKey"),
                        getRequiredProperty("sns.secretKey"),
                        getRequiredProperty("sns.region"),
                        env.getProperty("sns.endpoint")
                );
            case "kafka":
                if (kafkaTemplate == null) {
                    throw new IllegalStateException("KafkaTemplate is required for Kafka but not available.");
                }
                return new KafkaMessagePublisher(kafkaTemplate);
            default:
                throw new IllegalArgumentException("Invalid messaging provider: " + provider);
        }
    }

    private String getRequiredProperty(String key) {
        String value = env.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        return value;
    }
}
