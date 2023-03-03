package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.CharacterRequest;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@KafkaListener
public class CharactersListener {
    private final CharacterService characterService;

    @Topic("init-characters")
    public void receive(Flux<CharacterRequest> requests) {
        characterService.initCharacters(requests);
    }
}
