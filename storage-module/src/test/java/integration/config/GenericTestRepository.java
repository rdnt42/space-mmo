package integration.config;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenericTestRepository {
    private final DataSource dataSource;

    public GenericTestRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(
                sql,
                ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE
        );
    }

    public <V> int countRowsInTable(String table, String idName, V idValue) {
        String sql = "select count(*) from " + table + " where " + idName + "=?";

        try (
                PreparedStatement statement = getPreparedStatement(sql)
        ) {
            setParam(statement, 1, idValue);
            ResultSet rs = statement.executeQuery();

            return rs.last() ? rs.getRow() : 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void saveEntity(T object) {
        String table = getTableName(object.getClass());
        List<ImmutablePair<String, Object>> fields = getFields(object);

        String preparedColumns = fields.stream().map(field -> field.left)
                .collect(Collectors.joining(","));

        String preparedSeq = IntStream.range(0, fields.size())
                .mapToObj(i -> "?")
                .collect(Collectors.joining(","));

        String sql = "insert into " + table + "(" + preparedColumns + ") values(" + preparedSeq + ")";
        try (
                PreparedStatement statement = getPreparedStatement(sql)
        ) {
            for (int i = 0; i < fields.size(); i++) {
                setParam(statement, i + 1, fields.get(i).right);
            }
            statement.executeUpdate();
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

        return getFiledName(idFiled);
    }

    private <T> List<ImmutablePair<String, Object>> getFields(T object) {
        return FieldUtils.getAllFieldsList(object.getClass())
                .stream()
                .map(field -> new ImmutablePair<>(getFiledName(field), getFieldValue(field, object)))
                .toList();
    }

    private Object getFieldValue(Field field, Object object) {
        try {
            return FieldUtils.readField(field, object, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFiledName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).name();
        } else {
            return camelToSnakeCase(field.getName());
        }
    }

    private String camelToSnakeCase(String fieldName) {
        return fieldName.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2");
    }

    private <V> void setParam(PreparedStatement statement, int idx, V value) throws SQLException {
        switch (value) {
            case String str -> statement.setString(idx, str);
            case Long l -> statement.setLong(idx, l);
            case Integer i -> statement.setInt(idx, i);
            case Double d -> statement.setDouble(idx, d);
            case Boolean b -> statement.setBoolean(idx, b);
            case Short s -> statement.setShort(idx, s);
            case null, default -> throw new UnsupportedOperationException();
        }
    }
}
