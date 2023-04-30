package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.service.broker.CharactersClient;
import marowak.dev.service.motion.PlayerMotionService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
//    private final CharactersUpdateClient charactersUpdateClient;
    private final CharactersClient charactersClient;
    private final PlayerMotionService playerMotionService;

    @Override
    public void sendCharactersUpdate() {
        Collection<PlayerMotion> motions = playerMotionService.getAllMotions();
        if (motions.isEmpty()) {
            return;
        }

        List<CharacterMessage> messages = motions.stream()
                .map(this::convertToUpdateMessage)
                .toList();

//        charactersUpdateClient.sendCharacters(messages)
//                .doOnError(e -> log.error("Send failed", e))
//                .doOnNext(r -> log.debug("Send message for updating characters"))
//                .subscribe();
    }

    @Override
    public void initCharacters(Flux<CharacterMessage> requests) {
        requests
                .doOnNext(c -> {
                    playerMotionService.addMotion(c);
                    log.info("Character init successful, key: {}, character name: {}", c.getKey(), c.getCharacterName());
                })
                .subscribe();
    }

    @Override
    public void sendCharacterState(String characterName, boolean isOnline) {
        CharacterMessage message = CharacterMessage.builder()
                .key(CharacterMessageKey.CHARACTER_STATE_UPDATE)
                .characterName(characterName)
                .online(isOnline)
                .build();
//        charactersUpdateClient.sendCharacter(message)
//                .doOnError(e -> log.error("Send failed", e))
//                .doOnNext(r -> log.debug("Send message for updating characters"))
//                .subscribe();
    }

    @Override
    public void sendInitCharacter(CharacterMessageKey key, String characterName) {
        CharacterMessage message = CharacterMessage.builder()
                .key(key)
                .characterName(characterName)
                .build();
//        charactersClient.sendInitCharacters(message)
//                .doOnError(e -> log.error("Send Characters init error, key: {}, name: {}, error: {}", key, characterName, e.getMessage()))
//                .doOnSuccess(c -> log.info("Send Character init successful, key: {}, name: {}", key, characterName))
//                .subscribe();
    }

    private CharacterMessage convertToUpdateMessage(PlayerMotion playerMotion) {
        return CharacterMessage.builder()
                .key(CharacterMessageKey.CHARACTER_MOTION_UPDATE)
                .characterName(playerMotion.playerName())
                .x(playerMotion.x())
                .y(playerMotion.y())
                .angle(playerMotion.angle())
                .build();
    }

}
