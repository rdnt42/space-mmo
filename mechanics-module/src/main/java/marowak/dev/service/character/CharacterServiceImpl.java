package marowak.dev.service.character;

import jakarta.inject.Singleton;
import keys.CharacterMessageKey;
import keys.CharactersGetMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.service.broker.CharactersClient;
import marowak.dev.service.motion.PlayerMotionService;
import message.CharacterMessage;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharactersUpdateClient charactersUpdateClient;
    private final CharactersClient charactersClient;
    private final PlayerMotionService playerMotionService;

    @Override
    public void sendCharactersUpdate() {
        Collection<PlayerMotion> motions = playerMotionService.getAllMotions();
        if (motions.isEmpty()) {
            return;
        }

        List<CharacterMessage> requests = motions.stream()
                .map(this::convertPlayerMotion)
                .toList();

        List<CharacterMessageKey> key = Collections.singletonList(CharacterMessageKey.CHARACTER_MOTION_UPDATE);
        charactersUpdateClient.sendCharacters(key, requests)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .subscribe();
    }

    @Override
    public void initCharacters(Flux<CharacterMessage> requests) {
        requests
                .doOnNext(c -> {
                    playerMotionService.addMotion(c);
                    log.info("Character init successful, name: {}", c.getCharacterName());
                })
                .subscribe();
    }

    @Override
    public void sendCharacterState(String characterName, boolean isOnline) {
        CharacterMessage message = CharacterMessage.builder()
                .characterName(characterName)
                .online(isOnline)
                .build();
        charactersUpdateClient.sendCharacter(CharacterMessageKey.CHARACTER_STATE_UPDATE, message)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .subscribe();
    }

    @Override
    public void sendInitCharacter(CharactersGetMessageKey key, String characterName) {
        charactersClient.sendInitCharacters(key, characterName)
                .doOnError(e -> log.error("Send Characters init error, error: {}", e.getMessage()))
                .doOnSuccess(c -> log.info("Send Character init successful"))
                .subscribe();
    }

    private CharacterMessage convertPlayerMotion(PlayerMotion playerMotion) {
        return CharacterMessage.builder()
                .characterName(playerMotion.playerName())
                .x(playerMotion.x())
                .y(playerMotion.y())
                .angle(playerMotion.angle())
                .build();
    }

}
