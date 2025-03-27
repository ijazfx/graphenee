package io.graphenee.aws.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.graphenee.aws.messaging.factory.MessagePublisherFactory;
import io.graphenee.aws.messaging.publisher.MessagePublisher;

@Service
public class MessagingService {

    private final MessagePublisher messagePublisher;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessagingService(MessagePublisherFactory factory, Environment env) {
        String provider = env.getProperty("messaging.provider");
        this.objectMapper = new ObjectMapper();
        this.messagePublisher = factory.createPublisher(provider);
    }

    public String publishMessage(String topic, Object payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            return messagePublisher.publish(topic, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing message", e);
        }
    }
}
