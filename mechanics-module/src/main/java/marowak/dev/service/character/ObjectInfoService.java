package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.CharacterView;
import marowak.dev.api.response.item.ItemInSpace;
import marowak.dev.dto.Point;
import marowak.dev.service.item.SpaceItemService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ObjectInfoService {
    private final CharacterShipService characterShipService;
    private final SpaceItemService spaceItemService;

    public Mono<CharacterView> getCharacter(String characterName) {
        return characterShipService.getCharacter(characterName);
    }

    public Flux<CharacterView> getCharactersInRange(String characterName) {
        return characterShipService.getCharactersInRange(characterName);
    }

    public Flux<ItemInSpace> getItemsInRange(String characterName) {
        return characterShipService.getCharacter(characterName)
                .flatMapMany(character -> spaceItemService.getItemsInRange(new Point(character.x(), character.y())));
    }

    public Flux<CharacterView> getAllCharacters() {
        return characterShipService.getAllCharacters();
    }
}
