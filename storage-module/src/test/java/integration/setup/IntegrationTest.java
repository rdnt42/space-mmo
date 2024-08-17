package integration.setup;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.flywaydb.core.Flyway;

import java.util.HashMap;
import java.util.Map;

public abstract class IntegrationTest implements TestPropertyProvider {
    protected static final Map<String, String> properties = new HashMap<>();
    private static final DbContainerSetup dbSetup;
    private static final KafkaContainerSetup kafkaSetup;

    protected final GenericTestRepository repository;

    static {
        dbSetup = new DbContainerSetup();
        kafkaSetup = new KafkaContainerSetup();

        dbSetup.start(properties);
        kafkaSetup.start(properties);

        properties.put("flyway.datasources.jdbc.enabled", "false");
        Flyway.configure()
                .dataSource(dbSetup.dataSource)
                .load()
                .migrate();
    }

    protected IntegrationTest() {
        this.repository = new GenericTestRepository(dbSetup.dataSource);
    }


    @Override
    public @NonNull Map<String, String> getProperties() {
        return properties;
    }

}
