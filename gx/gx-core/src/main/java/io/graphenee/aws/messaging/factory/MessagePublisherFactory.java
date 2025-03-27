package io.graphenee.aws.messaging.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.graphenee.aws.messaging.publisher.MessagePublisher;
import io.graphenee.aws.messaging.publisher.kafka.KafkaMessagePublisher;
import io.graphenee.aws.messaging.publisher.sns.SnsMessagePublisher;

@Component
public class MessagePublisherFactory {

    private final Environment env;

    @Autowired
    public MessagePublisherFactory(Environment env) {
        this.env = env;
    }

    private String getRequiredProperty(String key) {
        String value = env.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        return value;
    }

    private Boolean getRequiredBoolProperty(String key) {
        String value = env.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required property: " + key);
        }
        return Boolean.parseBoolean(value);
    }

    public MessagePublisher createPublisher(String provider) {
        switch (provider.toLowerCase()) {
        case "sns":
            return new SnsMessagePublisher(getRequiredProperty("sns.accessKey"), getRequiredProperty("sns.secretKey"), getRequiredProperty("sns.region"),
                    env.getProperty("sns.endpoint"));
        case "kafka":
            return null;
        // return new KafkaMessagePublisher(getRequiredProperty("kafka.bootstrapServers"), getRequiredBoolProperty("kafka.use-aws-msk-bootstrap-servers"));
        default:
            throw new IllegalArgumentException("Invalid messaging provider: " + provider);
        }
    }
}
