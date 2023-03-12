package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@KafkaClient(batch = true)
public interface CharactersClient {

    @Topic("characters")
    Mono<RecordMetadata> sendCharacters(@KafkaKey List<CharacterMessageKey> keys, Collection<CharacterRequest> requests);

    @Topic("get-characters")
    Mono<RecordMetadata> getCharacters(Collection<Object> empty);
}
