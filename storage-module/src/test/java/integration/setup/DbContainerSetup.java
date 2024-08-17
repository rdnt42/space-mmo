package integration.setup;

import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static java.lang.String.format;

@Testcontainers
public class DbContainerSetup {
    @Container
    private final PostgreSQLContainer<?> postgres;

    private static final String postgreImage = "postgres:16-alpine";
    private static final String user = "user";
    private static final String pass = "password";
    private static final String dbName = "space_mmo_storage";

    public final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    public DbContainerSetup() {
        // TODO props
        postgres = new PostgreSQLContainer<>(postgreImage);
        postgres.withReuse(true)
                .withUsername(user)
                .withPassword(pass)
                .withDatabaseName(dbName);
    }

    public void start(Map<String, String> properties) {
        postgres.start();

        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDatabaseName(dbName);
        dataSource.setUrl(postgres.getJdbcUrl());

        properties.putAll(getProperties());
    }

    public Map<String, String> getProperties() {
        return Map.of(
                "r2dbc.datasources.default.url", format("r2dbc:postgresql://%s:%d/%s",
                        postgres.getHost(),
                        postgres.getFirstMappedPort(),
                        dbName),
                "r2dbc.datasources.default.username", user,
                "r2dbc.datasources.default.password", pass,
                "r2dbc.datasources.default.driverClassName", "org.postgresql.Driver",
                "datasources.jdbc.url", postgres.getJdbcUrl(),
                "datasources.jdbc.username", user,
                "datasources.jdbc.password", pass,
                "datasources.jdbc.driverClassName", "org.postgresql.Driver"
        );
    }
}
