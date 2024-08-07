package integration;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


@Testcontainers
public abstract class IntegrationTest implements TestPropertyProvider {
    private static final String POSTGRE_SQL_IMAGE = "postgres:16-alpine";
    private static final String KAFKA_IMAGE = "apache/kafka:3.8.0";

    private static final PGSimpleDataSource dataSource = new PGSimpleDataSource();

    @Container
    static PostgreSQLContainer<?> postgres;

    @Container
    static KafkaContainer kafka = new KafkaContainer(KAFKA_IMAGE);

    static {
        var pass = "password";
        var user = "user";
        var db = "space_mmo_storage";
        postgres = new PostgreSQLContainer<>(POSTGRE_SQL_IMAGE)
                .withReuse(true)
                .withUsername(user)
                .withPassword(pass)
                .withDatabaseName(db);

        postgres.start();
        kafka.start();

        var url = postgres.getJdbcUrl();

        Flyway.configure()
                .dataSource(url, user, pass)
                .load()
                .migrate();

        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDatabaseName(db);
        dataSource.setUrl(url);
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        return Collections.singletonMap("kafka.bootstrap.servers", kafka.getBootstrapServers());
    }

    public <T, U> boolean isEntityExists(Class<T> tClass, U expectedId) {
        return countRowsInTable(getTableName(tClass), getIdColumnName(tClass), expectedId) > 0;
    }

    public <V> int countRowsInTable(String table, String idName, V idValue) {
        String sql = "select count(*) from " + table + " where " + idName + "=?";

        try (PreparedStatement statement = dataSource.getConnection().prepareStatement(
                sql,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ) {
            setParam(statement, 1, idValue);
            ResultSet rs = statement.executeQuery();

            return rs.last() ? rs.getRow() : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> String getTableName(Class<T> tableClass) {
        return tableClass.getAnnotation(Table.class)
                .name();
    }

    private <T> String getIdColumnName(Class<T> tableClass) {
        Field idFiled = Arrays.stream(tableClass.getDeclaredFields())
                .filter((field -> field.isAnnotationPresent(Id.class)))
                .findFirst()
                .orElseThrow();

        if (idFiled.isAnnotationPresent(Column.class)) {
            return idFiled.getName();
        } else {
            return idFiled.getName()
                    .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                    .replaceAll("([a-z])([A-Z])", "$1_$2");
        }
    }

    private <V> void setParam(PreparedStatement statement, int paramIndex, V value) throws SQLException {
        if (value instanceof String str) {
            statement.setString(paramIndex, str);
        } else if (value instanceof Long l) {
            statement.setLong(paramIndex, l);
        }
    }
}
