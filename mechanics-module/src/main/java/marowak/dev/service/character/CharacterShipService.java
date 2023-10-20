package marowak.dev.service.character;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.character.CharacterShip;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.CharacterInfo;
import marowak.dev.response.InventoryInfo;
import marowak.dev.service.physic.Calculable;
import marowak.dev.service.physic.ShipService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Singleton
public class CharacterShipService implements Calculable {
    private final ShipService shipService;

    private final Map<String, CharacterShip> charactersMap = new ConcurrentHashMap<>();

    public Mono<Void> addCharacter(CharacterMotion motion) {
        return shipService.addShip(motion)
                .mapNotNull(body -> charactersMap.putIfAbsent(motion.characterName(), new CharacterShip(motion.characterName(), body)))
                .then();
    }

    public Mono<Void> removeCharacter(String characterName) {
        return shipService.removeShip(characterName)
                .mapNotNull(empty -> charactersMap.remove(characterName))
                .then();
    }

    public Mono<Void> addItem(String characterName, Item item) {
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

        return Mono.just(ship.getView());
    }

    public Flux<CharacterInfo> getAllCharacters() {
        return Flux.fromStream(charactersMap.values().stream())
                .map(CharacterShip::getView);
    }


    public Flux<CharacterInfo> getCharactersInRange(String characterName) {
        CharacterShip curr = charactersMap.get(characterName);

        return Flux.fromStream(charactersMap.values().stream())
                .filter(other -> other.isInRange(curr))
                .map(CharacterShip::getView);
    }

    public Mono<InventoryInfo> getInventoryInfo(String characterName) {
        CharacterShip curr = charactersMap.get(characterName);

        return Mono.just(curr.getInventoryView());
    }

    public Mono<Void> updateShooting(CharacterShootingRequest request, String characterName) {
        CharacterShip curr = charactersMap.get(characterName);
        curr.updateShootingState(request);

        return Mono.empty();
    }

    public Mono<Void> updateShipPosition(CharacterMotionRequest request, String characterName) {
        CharacterShip curr = charactersMap.get(characterName);
        curr.updateShipPosition(request);

        return Mono.empty();
    }

    @Async
    @Override
    public void calculate() {
        // TODO make shoots
    }
}
