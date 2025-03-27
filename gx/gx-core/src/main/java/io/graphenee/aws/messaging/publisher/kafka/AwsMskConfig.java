package io.graphenee.aws.messaging.publisher.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AwsMskConfig {

    @Value("${kafka.bootstrap-server.aws-msk}")
    private String kafkaBootstrapServerAwsMsk;

    void addMskIamProperties(Map<String, Object> props) {
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServerAwsMsk);
        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "AWS_MSK_IAM");
        props.put("sasl.jaas.config", "software.amazon.msk.auth.iam.IAMLoginModule required awsProfileName=\"default\";");
        props.put("sasl.client.callback.handler.class", "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
    }
}
