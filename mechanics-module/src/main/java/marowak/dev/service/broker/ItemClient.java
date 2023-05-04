package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import message.ItemMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

@KafkaClient(id = "items-producer")
public interface ItemClient {
    @Topic("items")
    Mono<RecordMetadata> sendItems(ItemMessage message);
}
