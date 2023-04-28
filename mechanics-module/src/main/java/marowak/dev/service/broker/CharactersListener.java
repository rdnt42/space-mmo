package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.service.character.CharacterService;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@KafkaListener
public class CharactersListener {
    private final CharacterService characterService;

    @Topic("init-characters")
    public void receive(Flux<CharacterMotionRequest> requests) {
        characterService.initCharacters(requests);
    }
}
