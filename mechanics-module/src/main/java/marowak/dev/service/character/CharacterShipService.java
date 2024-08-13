package marowak.dev.service.character;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.CharacterMotionRequest;
import marowak.dev.api.request.CharacterShootingRequest;
import marowak.dev.api.response.CharacterView;
import marowak.dev.character.CharacterShip;
import marowak.dev.character.Hull;
import marowak.dev.character.Item;
import marowak.dev.dto.Point;
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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
        if (item instanceof Hull) {
            SpaceShipBody shipBody = ship.createShipBody();
            worldService.createBody(shipBody);
        }
        return Mono.just(item);
    }

    public Mono<Item> updateItem(String characterName, Item updateItem) {
        CharacterShip ship = charactersMap.get(characterName);
        return Mono.just(ship.updateItem(updateItem));
    }

    public Mono<CharacterView> getCharacter(String characterName) {
        CharacterShip ship = charactersMap.get(characterName);

        return Mono.justOrEmpty(ship.getView());
    }

    public Mono<CharacterShip> getShip(String characterName) {
        CharacterShip ship = Optional.ofNullable(charactersMap.get(characterName))
                .orElseThrow();

        return Mono.just(ship);
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
            calculateShooting(ship);
            calculateDamage(ship);
        }
    }

    private void calculateShooting(CharacterShip ship) {
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
            spaceItemService.tryDropItemToSpace(item.getId(), coords).subscribe();
        }

        characterInformerSocketService.sendExplosionToAll(ship.getId())
                .subscribe();
    }

}
