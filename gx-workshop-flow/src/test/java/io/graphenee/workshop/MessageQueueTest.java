package io.graphenee.workshop;

import io.graphenee.aws.messaging.Payload;
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
            String response = messagingService.publishMessage(topic,"Hello, test message!");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
