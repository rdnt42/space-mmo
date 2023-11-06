package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.CharacterView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterInfoService {
    private final CharacterShipService characterShipService;

    public Mono<CharacterView> getCharacterInfo(String playerName) {
        return characterShipService.getCharacter(playerName);
    }

    public Flux<CharacterView> getCharactersInRangeInfo(String playerName) {
        return characterShipService.getCharactersInRange(playerName);
    }

    public Flux<CharacterView> getAllMotions() {
        return characterShipService.getAllCharacters();
    }
}
