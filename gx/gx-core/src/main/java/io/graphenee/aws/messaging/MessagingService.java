package io.graphenee.aws.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.graphenee.aws.messaging.factory.MessagePublisherFactory;
import io.graphenee.aws.messaging.publisher.MessagePublisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

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
