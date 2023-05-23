package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.service.broker.CharactersClient;
import marowak.dev.service.motion.CharacterMotionService;
import message.CharacterMessage;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharactersClient charactersClient;
    private final CharacterMotionService characterMotionService;

    @Override
    public void sendCharactersUpdate() {
        characterMotionService.getAllMotions()
                .map(motionToMessage)
                .doOnError(e -> log.error("Send characters updating failed", e))
                .flatMap(charactersClient::sendCharacters)
                .subscribe();
    }

    @Override
    public void initCharacters(CharacterMessage message) {
        characterMotionService.addMotion(message);
        log.info("Character init successful, key: {}, character name: {}", message.getKey(), message.getCharacterName());
    }

    @Override
    public void sendCharacterState(String characterName, boolean isOnline) {
        CharacterMessage message = CharacterMessage.builder()
                .key(CharacterMessageKey.CHARACTER_STATE_UPDATE)
                .characterName(characterName)
                .online(isOnline)
                .build();

        charactersClient.sendCharacters(message)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .subscribe();
    }

    @Override
    public void sendInitCharacter(CharacterMessageKey key, String characterName) {
        CharacterMessage message = CharacterMessage.builder()
                .key(key)
                .characterName(characterName)
                .build();

        charactersClient.sendCharacters(message)
                .doOnError(e -> log.error("Send Characters init error, key: {}, name: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send Character init, key: {}, name: {}", key, characterName))
                .subscribe();
    }

    private final Function<CharacterMotion, CharacterMessage> motionToMessage = motion -> CharacterMessage.builder()
            .key(CharacterMessageKey.CHARACTER_MOTION_UPDATE)
            .characterName(motion.characterName())
            .x(motion.x())
            .y(motion.y())
            .angle(motion.angle())
            .build();

}
