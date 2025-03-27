package io.graphenee.aws.messaging.publisher.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
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

    @Value("${kafka.bootstrap-server.local}")
    private String kafkaBootstrapServerLocal;

    @Value("${kafka.use-aws-msk-bootstrap-servers}")
    private Boolean useAwsMskBootstrapServers;

    @Autowired
    private AwsMskConfig awsMskConfig;

    @Bean("producerConfig1")
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServerLocal);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180_000); // default is 30_000
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 8192); // default is 16384

        if (useAwsMskBootstrapServers) {
            awsMskConfig.addMskIamProperties(props);
        }

        return props;
    }

    @Bean("producerFactory1")
    public ProducerFactory<Object, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean("kafkaTemplate1")
    public KafkaTemplate<Object, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
