package io.graphenee.workshop;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import io.graphenee.aws.messaging.MessagingService;

@SpringBootTest
public class MessageQueueTest {

    @Autowired
    private MessagingService messagingService;

    @Value("${messaging.provider}")
    private String provider;

    @Test
    void sendTestMessage() {
        try {
            String snsTopic = "arn:aws:sns:us-east-1:000000000000:my-topic";
            String kafkaTopic = "my-topic";
            String response = null;
            TestMessageDto messageDto = new TestMessageDto("TestID-123", "Hello, this is a test message!");
            if ("kafka".equalsIgnoreCase(provider))
                response = messagingService.publishMessage(kafkaTopic, messageDto);
            else if ("sns".equalsIgnoreCase(provider))
                response = messagingService.publishMessage(snsTopic, messageDto);
            System.out.println(provider + " Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Sample DTO class
    @Data
    static class TestMessageDto {
        private String messageId;
        private String messageContent;

        public TestMessageDto(String messageId, String messageContent) {
            this.messageId = messageId;
            this.messageContent = messageContent;
        }
    }
}
