package marowak.dev.service.character;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.api.request.CharacterShootingRequest;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.CharacterView;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.character.CharacterShip;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import marowak.dev.service.item.SpaceItemService;
import marowak.dev.service.physic.Calculable;
import marowak.dev.service.physic.WeaponService;
import marowak.dev.service.physic.WorldService;
import marowak.dev.service.socket.CharacterInformerSocketService;
import org.dyn4j.dynamics.Body;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static marowak.dev.enums.StorageType.STORAGE_TYPE_HULL;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterShipService implements Calculable {
    private final WeaponService weaponService;
    private final WorldService worldService;
    private final CharacterInformerSocketService characterInformerSocketService;
    private final SpaceItemService spaceItemService;

    private final Map<String, CharacterShip> charactersMap = new ConcurrentHashMap<>();

    public Mono<CharacterShip> addCharacter(CharacterMotion motion) {
        CharacterShip ship = new CharacterShip(motion.characterName(), new Point(motion.x(), motion.y()), motion.angle());
        charactersMap.put(ship.getId(), ship);

        return Mono.just(ship);
    }

    public Mono<Void> removeCharacter(String characterName) {
        CharacterShip ship = charactersMap.remove(characterName);
        List<Body> bodiesToDestroy = ship.destroy();
        worldService.removeBodies(bodiesToDestroy);

        return Mono.empty();
    }

    public Mono<Item> addItem(String characterName, Item item) {
        CharacterShip ship = charactersMap.get(characterName);

        ship.addItem(item);
        if (STORAGE_TYPE_HULL.equals(item.getStorageId()) && item instanceof Hull) {
            SpaceShipBody shipBody = ship.createShipBody();
            worldService.createBody(shipBody);
        }
        return Mono.just(item);
    }

    public Mono<Item> updateItem(String characterName, ItemUpdate request) {
        CharacterShip ship = charactersMap.get(characterName);

        Item item = ship.updateItem(request.id(), request.slotId(), request.storageId());

        return Mono.just(item);
    }

    public Mono<CharacterView> getCharacter(String characterName) {
        CharacterShip ship = charactersMap.get(characterName);

        return Mono.justOrEmpty(ship.getView());
    }

    public Flux<CharacterView> getAllCharacters() {
        return Flux.fromStream(charactersMap.values().stream())
                .mapNotNull(CharacterShip::getView);
    }


    public Flux<CharacterView> getCharactersInRange(String characterName) {
        CharacterShip curr = charactersMap.get(characterName);

        return Flux.fromStream(charactersMap.values().stream())
                .filter(curr::isInRange)
                .mapNotNull(CharacterShip::getView);
    }

    public Mono<InventoryView> getInventory(String characterName) {
        CharacterShip curr = charactersMap.get(characterName);

        return Mono.just(curr.getInventoryView());
    }

    public Mono<ItemView> getItem(String characterName, long itemId) {
        CharacterShip curr = charactersMap.get(characterName);

        return Mono.just(curr.getItem(itemId)
                .getView());
    }

    public Mono<Void> updateShooting(CharacterShootingRequest request, String characterName) {
        CharacterShip curr = charactersMap.get(characterName);
        curr.updateShootingState(request);

        return Mono.empty();
    }

    public Mono<Void> updateShipMotion(CharacterMotionRequest request, String characterName) {
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
        var result = ship.calculateDamage();
        if (result != null && result.isDead()) {
            blowUpShip(ship, result.killerId());
        }
    }

    private void blowUpShip(CharacterShip ship, String killerId) {
        log.info("Character '{}' was killed by '{}'", ship.getId(), killerId);
        CharacterShip remove = charactersMap.remove(ship.getId());
        Point coords = remove.getCoords();

        worldService.removeBodies(ship.destroy());
        for (Item item : ship.getItemsMap().values()) {
            spaceItemService.tryDropItemToSpace(item, coords).subscribe();
        }

        characterInformerSocketService.sendExplosionToAll(ship.getId())
                .subscribe();
    }

}
