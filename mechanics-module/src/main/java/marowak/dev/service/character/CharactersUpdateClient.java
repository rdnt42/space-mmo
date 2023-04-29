package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import keys.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@KafkaClient(batch = true)
public interface CharactersUpdateClient {

    @Topic("characters")
    Mono<RecordMetadata> sendCharacters(@KafkaKey List<CharacterMessageKey> keys, Collection<CharacterRequest> requests);

    @Topic("characters")
    Mono<RecordMetadata> sendCharacter(@KafkaKey CharacterMessageKey key, CharacterRequest request);

}
