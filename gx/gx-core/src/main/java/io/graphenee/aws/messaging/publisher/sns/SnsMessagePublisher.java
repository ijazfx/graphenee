package io.graphenee.aws.messaging.publisher.sns;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.graphenee.aws.messaging.publisher.MessagePublisher;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

public class SnsMessagePublisher implements MessagePublisher {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    public SnsMessagePublisher(String awsAccessKey, String awsSecretKey, String region, String endpoint) {
        this.objectMapper = new ObjectMapper();
        this.snsClient = SnsClient.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .endpointOverride(java.net.URI.create(endpoint)) // <-- LocalStack support
                .build();
    }

    public SnsMessagePublisher(String awsAccessKey, String awsSecretKey, String region) {
        this.objectMapper = new ObjectMapper();
        this.snsClient = SnsClient.builder().region(Region.of(region)).credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .build();
    }

    @Override
    public String publish(String topicArn, String payload) {
        try {
            String message = objectMapper.writeValueAsString(payload);
            PublishRequest request = PublishRequest.builder().message(message).topicArn(topicArn).build();
            PublishResponse response = snsClient.publish(request);
            return "SNS Message ID: " + response.messageId();
        } catch (Exception e) {
            throw new RuntimeException("Error publishing to SNS", e);
        }
    }
}
