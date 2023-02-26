package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import reactor.core.publisher.Mono;

import java.util.Collection;

@KafkaClient
public interface CharacterClient {

    @Topic("characters")
    Mono<CharacterRequest> sendCharacter(@KafkaKey CharacterMessageKey key, Collection<CharacterRequest> requests);
}
