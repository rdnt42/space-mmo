package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.character.CharacterService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@KafkaListener
public class CharactersListener {
    private final CharacterService characterService;

    @Topic("characters-answer")
    public void receive(Flux<CharacterMessage> requests) {
        characterService.initCharacters(requests);
    }
}
