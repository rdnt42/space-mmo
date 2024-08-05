package integration;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.util.Collections;
import java.util.Map;


@Testcontainers
public abstract class IntegrationTest implements TestPropertyProvider {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    )
            .withReuse(true)
            .withDatabaseName("space_mmo_storage")
            .withPassword("user")
            .withUsername("password");

    @Container
    static KafkaContainer kafka = new KafkaContainer("apache/kafka:3.8.0");

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!kafka.isRunning()) {
            kafka.start();
        }

        return Collections.singletonMap("kafka.bootstrap.servers", kafka.getBootstrapServers());
    }
}
