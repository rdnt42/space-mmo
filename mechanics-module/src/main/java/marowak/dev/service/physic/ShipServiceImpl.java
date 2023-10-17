package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Weapon;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.KineticBullet;
import marowak.dev.dto.world.SpaceShip;
import marowak.dev.enums.ForceType;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.item.ItemService;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ShipServiceImpl implements ShipService, Calculable {

    private final WorldService worldService;
    private final ItemService itemService;

    @Override
    public Mono<Void> addShip(CharacterMotion motion) {
        SpaceShip ship = FactoryUtils.createShip(motion);
        worldService.createBody(ship);

        return Mono.empty();
    }

    @Override
    public Mono<Void> updateShip(CharacterMotionRequest request, String characterName) {
        SpaceShip ship = worldService.getBody(SpaceShip.class, characterName);

        return itemService.getFirstEquippedItem(characterName, ItemTypes.ITEM_TYPE_ENGINE)
                .map(Engine.class::cast)
                .flatMap(engine -> applyForce(ship, engine.getSpeed(), request.angle(), request.forceTypeId()));
    }

    @Override
    public Mono<Void> updateShooting(CharacterShootingRequest request, String characterName) {
        return Mono.just(worldService.getBody(SpaceShip.class, characterName))
                .mapNotNull(ship -> {
                    ship.setShooting(request.isShooting());
                    double angleInRadians = Math.toRadians(request.angle());
                    ship.setShootAngleRadians((float) angleInRadians);

                    return null;
                });
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
        if (ship.isShooting()) {
            Vector2 translation = ship.getTransform().getTranslation();

            itemService.getEquippedItems(ship.getId(), ItemTypes.ITEM_TYPE_WEAPON)
                    .map(Weapon.class::cast)
                    .mapNotNull(weapon -> {
                        BulletCreateRequest request =
                                getNewBullet(ship.getShootAngleRadians(), translation.x, translation.y, weapon);
                        KineticBullet bullet = FactoryUtils.createKineticBullet(request);
                        worldService.createBody(bullet);

                        return null;
                    }).subscribe()
            ;
        }
    }

    // TODO template
    private BulletCreateRequest getNewBullet(double angle, double baseX, double baseY, Weapon weapon) {
        int shiftX = 0;
        int shiftY = 0;

        return new BulletCreateRequest(angle, baseX + shiftX, baseY + shiftY);
    }
}
