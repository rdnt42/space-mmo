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
        return switch (message.getKey()) {
            case CHARACTERS_GET_ONE, CHARACTERS_GET_ALL -> characterService.initCharacter(message);
            default -> Mono.empty();
        };

    }
}
