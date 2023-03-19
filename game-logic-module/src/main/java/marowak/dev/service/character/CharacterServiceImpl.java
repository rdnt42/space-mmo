package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.CharactersGetMessageKey;
import marowak.dev.enums.CharactersUpdateMessageKey;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterRequest;
import marowak.dev.request.CharacterStateRequest;
import marowak.dev.service.motion.PlayerMotionService;
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

        List<CharacterRequest> requests = motions.stream()
                .map(this::convertPlayerMotion)
                .toList();

        List<CharactersUpdateMessageKey> key = Collections.singletonList(CharactersUpdateMessageKey.CHARACTER_MOTION_UPDATE);
        charactersUpdateClient.sendCharacters(key, requests)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .subscribe();
    }

    @Override
    public void initCharacters(Flux<CharacterMotionRequest> requests) {
        requests
                .doOnNext(playerMotionService::addMotion)
                .subscribe();

    }

    @Override
    public void sendCharacterState(String characterName, boolean isOnline) {
        CharacterStateRequest request = new CharacterStateRequest(null, characterName, isOnline);
        charactersUpdateClient.sendCharacter(CharactersUpdateMessageKey.CHARACTER_STATE_UPDATE, request)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .subscribe();

    }

    @Override
    public void sendInitCharacter(CharactersGetMessageKey key, String characterName) {
        charactersClient.sendInitCharacters(key, characterName)
                .doOnError(e -> log.error("Characters init error, error: {}", e.getMessage()))
                .doOnSuccess(c -> log.info("Character init successful"))
                .subscribe();
    }

    private CharacterRequest convertPlayerMotion(PlayerMotion playerMotion) {
        return new CharacterMotionRequest(null, playerMotion.playerName(),
                playerMotion.x(), playerMotion.y(), playerMotion.angle());
    }

}
