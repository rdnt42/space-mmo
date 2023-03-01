package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import marowak.dev.service.motion.PlayerMotionService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
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

        List<CharacterMessageKey> key = Collections.singletonList(CharacterMessageKey.CHARACTER_UPDATE);
        charactersClient.sendCharacters(key, requests)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for updating characters"))
                .subscribe();
    }

    private CharacterRequest convertPlayerMotion(PlayerMotion playerMotion) {
        return new CharacterRequest(null, playerMotion.playerName(),
                playerMotion.motion().x(), playerMotion.motion().y(), playerMotion.motion().angle());
    }

}
