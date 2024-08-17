package integration.setup;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class KafkaJsonDeserializer<V> implements Deserializer<V> {
    private final JsonMapper jsonMapper = new JsonMapper();
    private final Class<V> type;

    public KafkaJsonDeserializer(Class<V> type) {
        this.type = type;
    }

    @Override
    public V deserialize(String topic, byte[] data) {
        try {
            return jsonMapper.readValue(data, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
