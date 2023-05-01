package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import message.EquipmentMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

@KafkaClient(id = "equipments-producer")
public interface EquipmentClient {
    @Topic("equipments")
    Mono<RecordMetadata> sendGetEquipments(EquipmentMessage message);
}
