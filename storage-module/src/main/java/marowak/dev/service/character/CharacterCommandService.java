package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.CharacterMessage;
import org.reactivestreams.Publisher;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterCommandService {
    private final CharacterService characterService;

    public Publisher<CharacterMessage> executeCommand(CharacterMessage message) {
        return switch (message.getKey()) {
            case CHARACTER_CREATE -> characterService.create(message);
            case CHARACTER_MOTION_UPDATE -> characterService.updateMotion(message);
            case CHARACTER_STATE_UPDATE -> characterService.updateState((message));
            case CHARACTERS_GET_ONE -> characterService.get(message.getCharacterName());
            case CHARACTERS_GET_ALL -> characterService.getAllOnline();
        };
    }

}
