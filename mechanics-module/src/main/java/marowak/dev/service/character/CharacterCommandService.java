package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import message.CharacterMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class CharacterCommandService {
    private final CharacterService characterService;

    public Publisher<Void> executeCommand(CharacterMessage message) {
        switch (message.getKey()) {
            case CHARACTERS_GET_ONE, CHARACTERS_GET_ALL -> characterService.initCharacters(message);
            case CHARACTER_MOTION_UPDATE, CHARACTER_STATE_UPDATE -> {
                // TODO add map updating
            }
            default -> { // Ignore other events
            }
        }

        return Mono.empty();
    }
}
