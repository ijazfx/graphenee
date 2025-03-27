package io.graphenee.aws.messaging.publisher;

public interface MessagePublisher {
    public String publish(String topic, String payload);
}
