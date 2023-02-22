package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@KafkaListener
public class CharacterListener {
    private final CharacterService characterService;

    @Topic("characters")
    public Publisher<Character> createCharacter(CharacterRequest request) {
        return characterService.create(request);
    }
}
