package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.PlayerMotion;
import marowak.dev.enums.CharacterMessageKey;
import marowak.dev.request.CharacterRequest;
import marowak.dev.service.motion.PlayerMotionService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterServiceImpl implements CharacterService {
    private final CharacterClient characterClient;
    private final PlayerMotionService playerMotionService;

    @Override
    public void sendCharactersUpdate() {
        Collection<PlayerMotion> motions = playerMotionService.getAllMotions();
        List<CharacterRequest> requests = motions.stream()
                .map(this::convertPlayerMotion)
                .collect(Collectors.toList());

        characterClient.sendCharacter(CharacterMessageKey.CHARACTER_UPDATE_ALL, requests)
                .doOnError(e -> log.error("Send failed", e))
                .doOnNext(r -> log.debug("Send message for create character {}, account {}", r.accountName(), r.characterName()))
                .subscribe();
    }

    private CharacterRequest convertPlayerMotion(PlayerMotion playerMotion) {
        return CharacterRequest.builder()
                .characterName(playerMotion.playerName())
                .x(playerMotion.motion().x())
                .y(playerMotion.motion().y())
                .angle(playerMotion.motion().angle())
                .build();
    }
}
