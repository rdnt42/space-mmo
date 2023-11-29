package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.BulletBodyView;
import marowak.dev.api.response.CharacterView;
import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.ItemDto;
import marowak.dev.service.character.CharacterShipService;
import marowak.dev.service.item.ItemStorage;
import marowak.dev.service.item.SpaceItemService;
import marowak.dev.service.physic.WeaponService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ObjectInfoService {
    private final CharacterShipService characterShipService;
    private final SpaceItemService spaceItemService;
    private final WeaponService weaponService;
    private final ItemStorage itemStorage;


    public Mono<CharacterView> getCharacter(String characterName) {
        return characterShipService.getCharacter(characterName);
    }

    public Flux<CharacterView> getCharactersInRange(String characterName) {
        return characterShipService.getCharactersInRange(characterName);
    }

    public Flux<ItemInSpaceView> getItemsInRange(String characterName) {
        return characterShipService.getCharacter(characterName)
                .flatMapMany(character -> spaceItemService.getItemsInRange(new Point(character.x(), character.y())));
    }

    public Flux<CharacterView> getAllCharacters() {
        return characterShipService.getAllCharacters();
    }

    public Flux<BulletBodyView> getBullets(String characterName) {
        return weaponService.getBulletsInRange(characterName);
    }

    public Mono<ItemView> getItem(long itemId) {
        return itemStorage.getItem(itemId)
                .map(ItemDto::getView);
    }

}
