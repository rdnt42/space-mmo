package marowak.dev.service;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import marowak.dev.entity.Character;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@KafkaListener(batch = true)
public class CharacterListener {
    private final CharacterCommandService characterCommandService;

    @Topic("characters")
    public List<Publisher<Character>> receive(@KafkaKey List<CharacterMessageKey> keys, List<CharacterRequest> requests) {
        List<Publisher<Character>> publishers = new ArrayList<>();
        for (int i = 0; i < requests.size(); i++) {
            Publisher<Character> publisher = characterCommandService.executeCommand(keys.get(i), requests.get(i));
            publishers.add(publisher);
        }

        return publishers;
    }
}
