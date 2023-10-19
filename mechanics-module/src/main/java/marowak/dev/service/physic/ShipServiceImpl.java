package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Weapon;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.SpaceShip;
import marowak.dev.enums.ForceType;
import marowak.dev.enums.ItemType;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.character.CharacterShipService;
import marowak.dev.service.item.ItemService;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ShipServiceImpl implements ShipService, Calculable {

    private final WorldService worldService;
    private final ItemService itemService;

    private final CharacterShipService characterShipService;

    @Override
    public Mono<Void> addShip(CharacterMotion motion) {
        SpaceShip ship = FactoryUtils.createShip(motion);
        worldService.createBody(ship);

        return Mono.empty();
    }

    @Override
    public Mono<Void> updateShip(CharacterMotionRequest request, String characterName) {
        SpaceShip body = worldService.getBody(SpaceShip.class, characterName);

        return characterShipService.getCharacter(characterName)
                .flatMap(ship -> applyForce(body, ship.shipTypeId().getSpeed(), request.angle(), request.forceTypeId()));
    }

    @Override
    public Mono<Void> updateShooting(CharacterShootingRequest request, String characterName) {
        return itemService.getEquippedItems(characterName, ItemType.ITEM_TYPE_WEAPON)
                .ofType(Weapon.class)
                .doOnNext(weapon -> weapon.changeShootState(request, new Point()))
                .then();
    }

    @Override
    public Mono<Void> deleteShip(String characterName) {
        SpaceShip ship = worldService.getBody(SpaceShip.class, characterName);
        worldService.removeBody(ship);

        return Mono.empty();
    }

    @Override
    public Flux<BodyInfo> getShipsInRange(String characterName) {
        Vector2 base = worldService.getBody(SpaceShip.class, characterName).getTransform().getTranslation();
        List<SpaceShip> ships = worldService.getBodies(SpaceShip.class);

        return Flux.fromStream(ships.stream())
                .filter(target -> Utils.isInRange(base, target.getTransform().getTranslation()))
                .map(Utils.bodyToBodyInfo);
    }

    @Override
    public Flux<BodyInfo> getAllShips() {
        List<SpaceShip> ships = worldService.getBodies(SpaceShip.class);

        return Flux.fromStream(ships.stream())
                .map(Utils.bodyToBodyInfo);
    }

    @Override
    public Mono<BodyInfo> getShip(String characterName) {
        return Mono.justOrEmpty(worldService.getBody(SpaceShip.class, characterName))
                .map(Utils.bodyToBodyInfo);
    }

    private Mono<Void> applyForce(Body body, int speed, float angle, int forceType) {
        double angleInRadians = Math.toRadians(angle);

        body.getTransform().setRotation(angleInRadians);
        if (ForceType.POSITIVE.equalsId(forceType)) {
            Vector2 r = new Vector2(body.getTransform().getRotationAngle());
            Vector2 f = r.product(5000.0 * speed);
            body.applyForce(f);
        } else if (ForceType.NEGATIVE.equalsId(forceType)) {
            Vector2 r = new Vector2(body.getTransform().getRotationAngle());
            Vector2 f = r.product(-5000.0 * speed);
            body.applyForce(f);
        } else if (ForceType.REVERSE.equalsId(forceType)) {
            Vector2 r = body.getLinearVelocity();
            Vector2 f = r.product(-15000);
            body.applyForce(f);
        }
        body.setAtRest(false);

        return Mono.empty();
    }

    @Async
    @Override
    public void calculate() {
        worldService.getBodies(SpaceShip.class)
                .forEach(this::calculateSpaceShip);
    }

    private void calculateSpaceShip(SpaceShip ship) {
        characterShipService.getCharacter(ship.getId())
                .doOnNext(ship -> ship)
    }

}
