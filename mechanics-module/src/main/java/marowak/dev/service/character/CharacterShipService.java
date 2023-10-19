package marowak.dev.service.character;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.character.CharacterShip;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.CharacterInfo;
import marowak.dev.service.physic.ShipService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@RequiredArgsConstructor
@Singleton
public class CharacterShipService {

    private final ShipService shipService;

    private final Map<String, CharacterShip> charactersMap = new ConcurrentHashMap<>();

    public Mono<Void> addShip(CharacterMotion motion) {
        charactersMap.putIfAbsent(motion.characterName(), new CharacterShip(motion.characterName()));
        CharacterShip ship = charactersMap.get(motion.characterName());
        ship.updateCoords(motion.x(), motion.y());

        return shipService.addShip(motion);
    }

    public Mono<Void> addItem(String characterName, Item item) {
        charactersMap.putIfAbsent(characterName, new CharacterShip(characterName));
        CharacterShip ship = charactersMap.get(characterName);

        ship.addItem(item);

        return Mono.empty();
    }

    public Mono<Item> updateItem(String characterName, ItemUpdate request) {
        CharacterShip ship = charactersMap.get(characterName);

        ship.updateItem(request.id(), request.slotId(), request.storageId());

        return Mono.empty();
    }

    public Mono<CharacterInfo> getCharacter(String characterName) {
        CharacterShip ship = charactersMap.get(characterName);
        return Mono.just(toCharacterResponse.apply(ship));
    }

    public Flux<Item> getInventoryItems(String characterName) {
        CharacterShip ship = charactersMap.get(characterName);
        return Flux.fromIterable(ship.getItemsMap().values());
    }

    private final Function<CharacterShip, CharacterInfo> toCharacterResponse =
            ship -> CharacterInfo.builder()
                    .characterName(ship.getId())
                    .x(ship.getX())
                    .y(ship.getY())
                    .angle(ship.getAngle())
                    .speed(ship.getSpeed())
                    .shipTypeId(ship.getHull().getTypeId())
                    .hp(ship.getHull().getHp())
                    .build();

}
