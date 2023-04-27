package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharactersGetMessageKey;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class CharacterInitListener {
    private final CharacterService characterService;

    @Topic("get-characters")
    @SendTo("init-characters")
    public Flux<Character> receive(@KafkaKey CharactersGetMessageKey key, String characterName) {
        log.info("Get init characters command: {}, character name: {}", key, characterName);
        return switch (key) {
            case CHARACTERS_GET_ONE -> Flux.from(characterService.get(characterName));
            case CHARACTERS_GET_ALL -> Flux.from(characterService.getAllOnline());
        };
    }
}
