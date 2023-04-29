package marowak.dev.service.character;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(batch = true)
public class CharacterListener {
    private final CharacterCommandService characterCommandService;

    @Topic("characters")
    public void receive(@KafkaKey List<CharacterMessageKey> keys, List<byte[]> requests) {
        if (keys.size() != requests.size()) {
            log.error("Null key for requests: {}", requests);
        }
        for (int i = 0; i < keys.size(); i++) {
            CharacterMessageKey key = keys.get(i);

            byte[] request = requests.get(i);
            characterCommandService.tryExecuteCommand(key, request);
        }
    }
}
