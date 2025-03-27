package io.graphenee.workshop;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.graphenee.aws.messaging.MessagingService;

@SpringBootTest
public class MessageQueueTest {

    @Autowired
    private MessagingService messagingService;

    @Test
    void sendTestMessage() {
        try {
            String topic = "arn:aws:sns:us-east-1:000000000000:my-topic"; // or Kafka topic
            TestMessageDto messageDto = new TestMessageDto("TestID-123", "Hello, this is a test message!");
            String response = messagingService.publishMessage(topic, messageDto);
            System.out.println("Response: " + response);
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
