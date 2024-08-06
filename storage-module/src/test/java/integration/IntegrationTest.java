package integration;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.support.TestPropertyProvider;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        Flyway flyway = Flyway
                .configure()
                .dataSource(url, user, pass)
                .load();
        flyway.migrate();

        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDatabaseName(db);
        dataSource.setUrl(url);
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        return Collections.singletonMap("kafka.bootstrap.servers", kafka.getBootstrapServers());
    }

    protected ResultSet performQuery(String sql) throws SQLException {
        Statement statement = dataSource.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();

        return resultSet;
    }

    public boolean isEntityExists(String table, String idName, String idValue) {
        return countRowsInTable(table, idName, idValue) > 0;
    }

    public int countRowsInTable(String table, String idName, String idValue) {
        StringBuilder sb = new StringBuilder()
                .append("select count(*) from ")
                .append(table)
                .append(" where ")
                .append(idName)
                .append("=?");

        String sql = sb.toString();


        try {
            PreparedStatement statement = dataSource.getConnection().prepareStatement(
                    sb.toString(),
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, idValue
            );
            ResultSet rs = statement.executeQuery();

            return rs.last() ? rs.getRow() : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


//        try (
//                Connection connection = dataSource.getConnection();
//                Statement stmt = connection.createStatement();
//                ResultSet rs = stmt.executeQuery(sql);
//        ) {
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }
}
