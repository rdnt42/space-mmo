package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.response.CharacterInfo;
import marowak.dev.service.character.CharacterShipService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInfoService {
    private final CharacterShipService characterShipService;

    public Mono<CharacterInfo> getCharacterInfo(String playerName) {
        return characterShipService.getCharacter(playerName);
    }

    public Flux<CharacterInfo> getCharactersInRangeInfo(String playerName) {
        return characterShipService.getCharactersInRange(playerName);
    }

    public Flux<CharacterInfo> getAllMotions() {
        return characterShipService.getAllCharacters();
    }
}
