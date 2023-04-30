package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import message.CharacterMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

@KafkaClient(id = "characters-producer")
public interface CharactersClient {
    @Topic("characters")
    Mono<RecordMetadata> sendInitCharacters(CharacterMessage message);
}
