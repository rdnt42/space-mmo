package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import keys.EquipmentMessageKey;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

@KafkaClient
public interface EquipmentClient {
    @Topic("equipments")
    Mono<RecordMetadata> sendGetEquipments(@KafkaKey EquipmentMessageKey key, String characterName);
}
