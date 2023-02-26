package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

import java.util.List;

@RequiredArgsConstructor
@KafkaListener(batch = true)
public class CharactersListener {
    private final CharacterService characterService;

    @Topic("characters")
    public Publisher<Character> createCharacter(List<CharacterRequest> requests) {
        return characterService.updateAllMotions(requests);
    }
}
