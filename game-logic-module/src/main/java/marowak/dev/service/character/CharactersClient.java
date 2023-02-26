package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import marowak.dev.request.CharacterRequest;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

import java.util.Collection;

@KafkaClient(batch=true)
public interface CharactersClient {

    @Topic("characters")
    Mono<RecordMetadata> sendCharacters(Collection<CharacterRequest> requests);
}
