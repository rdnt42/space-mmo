package marowak.dev.service.character;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.character.CharacterShip;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.CharacterInfo;
import marowak.dev.response.InventoryInfo;
import marowak.dev.service.physic.Calculable;
import marowak.dev.service.physic.ShipService;
import marowak.dev.service.physic.WeaponService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterShipService implements Calculable {
    private final ShipService shipService;
    private final WeaponService weaponService;

    private final Map<String, CharacterShip> charactersMap = new ConcurrentHashMap<>();

    public Mono<CharacterShip> addCharacter(CharacterMotion motion) {
        return shipService.addShip(motion)
                .mapNotNull(body -> {
                    CharacterShip ship = new CharacterShip(motion.characterName(), body);
                    charactersMap.put(ship.getId(), ship);

                    return ship;
                });
    }

    public Mono<Void> removeCharacter(String characterName) {
        return shipService.removeShip(characterName)
                .mapNotNull(empty -> charactersMap.remove(characterName))
                .then();
    }

    public Mono<Item> addItem(String characterName, Item item) {
        CharacterShip ship = charactersMap.get(characterName);

        ship.addItem(item);
        return Mono.just(item);
    }

    public Mono<Item> updateItem(String characterName, ItemUpdate request) {
        CharacterShip ship = charactersMap.get(characterName);

        Item item = ship.updateItem(request.id(), request.slotId(), request.storageId());

        return Mono.just(item);
    }

    public Mono<CharacterInfo> getCharacter(String characterName) {
        CharacterShip ship = charactersMap.get(characterName);

        return Mono.justOrEmpty(ship == null ? null : ship.getView());
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
        List<CharacterShip> ships = charactersMap.values().stream()
                .toList();
        for (CharacterShip ship : ships) {
            calculateSHooting(ship);
            calculateDamage(ship);
        }
    }

    private void calculateSHooting(CharacterShip ship) {
        List<BulletBody> bulletBodies = ship.useWeapons();
        if (bulletBodies.isEmpty()) return;

        bulletBodies.forEach(bullet -> weaponService.createBullet(bullet).subscribe());
    }

    private void calculateDamage(CharacterShip ship) {
        if (!ship.getShipBody().getAccumulatedDamage().isEmpty()) {
            log.info("Ship {} got damage {}", ship.getId(), ship.getShipBody().getDamage().damage());
        }
    }

}