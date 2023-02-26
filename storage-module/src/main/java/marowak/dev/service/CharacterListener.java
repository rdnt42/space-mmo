package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@KafkaListener
public class CharacterListener {
    private final CharacterCommandService characterCommandService;

    @Topic("character")
    public Publisher<Character> createCharacter(@KafkaKey CharacterMessageKey key, CharacterRequest request) {
        return characterCommandService.executeCommand(key, request);
    }
}
