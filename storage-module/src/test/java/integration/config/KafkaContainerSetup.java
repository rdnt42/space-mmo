package integration.config;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.util.Map;

@Testcontainers
public class KafkaContainerSetup {
    @Container
    private final KafkaContainer kafka;

    private static final String kafkaImage = "apache/kafka:3.8.0";


    public KafkaContainerSetup() {
        kafka = new KafkaContainer(kafkaImage);
    }

    public void start(Map<String, String> properties) {
        kafka.start();
        properties.putAll(getProperties());
    }

    public Map<String, String> getProperties() {
        return Map.of(
                "kafka.bootstrap.servers", kafka.getBootstrapServers()
        );
    }
}
