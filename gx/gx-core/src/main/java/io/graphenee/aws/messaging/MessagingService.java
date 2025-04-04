package io.graphenee.aws.messaging;

import org.springframework.stereotype.Service;

import io.graphenee.aws.messaging.factory.MessagePublisherFactory;
import io.graphenee.aws.messaging.publisher.MessagePublisher;

@Service
public class MessagingService {

    private final MessagePublisher messagePublisher;

    public MessagingService(MessagePublisherFactory factory) {
        this.messagePublisher = factory.createPublisher();
    }

    public String publishMessage(String topic, Object messageDto) {
        Payload<Object> payload = new Payload<>(messageDto);
        return messagePublisher.publish(topic, payload);
    }
}
