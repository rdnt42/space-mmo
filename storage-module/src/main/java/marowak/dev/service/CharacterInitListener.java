package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Character;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class CharacterInitListener {
    private final CharacterService characterService;

    @Topic("get-characters")
    @SendTo("init-characters")
    public Flux<Character> receive() {
        return Flux.from(characterService.getAll());
    }
}
