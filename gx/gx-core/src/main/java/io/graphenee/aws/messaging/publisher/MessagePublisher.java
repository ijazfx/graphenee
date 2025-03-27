package io.graphenee.aws.messaging.publisher;

import io.graphenee.aws.messaging.Payload;

public interface MessagePublisher {
    String publish(String topic, Payload<?> payload);
}
