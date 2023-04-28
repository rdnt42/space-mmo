package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import marowak.dev.enums.CharactersGetMessageKey;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

@KafkaClient
public interface CharactersClient {
    @Topic("get-characters")
    Mono<RecordMetadata> sendInitCharacters(@KafkaKey CharactersGetMessageKey key, String characterName);
}
