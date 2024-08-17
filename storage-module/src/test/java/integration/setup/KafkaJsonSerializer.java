package integration.setup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaJsonSerializer<V> implements Serializer<V> {
    private final JsonMapper jsonMapper = new JsonMapper();

    @Override
    public byte[] serialize(String topic, V data) {
        try {
            return jsonMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
