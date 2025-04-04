package io.graphenee.aws.messaging.publisher.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAsync
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-server}")
    private String kafkaBootstrapServer;

    @Value("${kafka.security.protocol:PLAINTEXT}") // Default to PLAINTEXT for local
    private String securityProtocol;

    @Value("${kafka.sasl.mechanism:#{null}}")
    private String saslMechanism;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Configure security only if not using plaintext
        if (!"PLAINTEXT".equals(securityProtocol)) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);

            if (saslMechanism != null) {
                props.put(SaslConfigs.SASL_MECHANISM, saslMechanism);

                // Configure for AWS MSK IAM
                if ("AWS_MSK_IAM".equals(saslMechanism)) {
                    props.put(SaslConfigs.SASL_JAAS_CONFIG,
                            "software.amazon.msk.auth.iam.IAMLoginModule required;");
                    props.put(SaslConfigs.SASL_CLIENT_CALLBACK_HANDLER_CLASS,
                            "software.amazon.msk.auth.iam.IAMClientCallbackHandler");
                }
            }
        }

        // Recommended producer settings
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        return props;
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
