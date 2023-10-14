package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.SpaceShip;
import marowak.dev.enums.BulletType;
import marowak.dev.enums.ForceType;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.item.ItemService;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Singleton
public class ShipServiceImpl implements ShipService, Calculable {

    private final WorldService worldService;
    private final ItemService itemService;
    private final Map<String, SpaceShip> ships = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> addShip(CharacterMotion motion) {
        SpaceShip ship = new SpaceShip(motion.characterName());

        BodyFixture bodyFixture = ship.addFixture(Geometry.createCircle(64 * 0.75));
        bodyFixture.setDensity(1);
        bodyFixture.setFriction(0.1);
        bodyFixture.setRestitution(0.3);
        bodyFixture.setRestitutionVelocity(0.001);
        ship.setLinearDamping(0.1);
        ship.setMass(MassType.NORMAL);
        ship.translate(motion.x(), motion.y());
        double angleInRadians = Math.toRadians(motion.angle());
        ship.getTransform().setRotation(angleInRadians);
        ship.setAtRestDetectionEnabled(false);

        worldService.createBody(ship);
        ships.put(ship.getId(), ship);

        return Mono.empty();
    }

    @Override
    public Mono<Void> updateShip(CharacterMotionRequest request, String characterName) {
        SpaceShip ship = ships.get(characterName);

        return itemService.getFirstEquippedItem(characterName, ItemTypes.ITEM_TYPE_ENGINE)
                .map(Engine.class::cast)
                .flatMap(engine -> applyForce(ship, engine.getSpeed(), request.angle(), request.forceTypeId()));
    }

    @Override
    public Mono<Void> deleteShip(String characterName) {
        SpaceShip ship = ships.get(characterName);
        if (worldService.removeBody(ship)) {
            ships.remove(characterName);
        }

        return Mono.empty();
    }

    @Override
    public Flux<BodyInfo> getShipsInRange(String characterName) {
        Vector2 base = ships.get(characterName).getTransform().getTranslation();

        return Flux.fromStream(ships.entrySet().stream())
                .filter(target -> Utils.isInRange(base, target.getValue().getTransform().getTranslation()))
                .map(entry -> Utils.bodyToBodyInfo.apply(entry.getValue(), entry.getKey()));
    }

    @Override
    public Flux<BodyInfo> getAllShips() {
        return Flux.fromStream(ships.entrySet().stream())
                .map(entry -> Utils.bodyToBodyInfo.apply(entry.getValue(), entry.getKey()));
    }

    @Override
    public Mono<BodyInfo> getShip(String characterName) {
        return Mono.justOrEmpty(ships.get(characterName))
                .map(ship -> Utils.bodyToBodyInfo.apply(ship, characterName));
    }

    @Override
    public Mono<SpaceShip> getShipBody(String characterName) {
        return Mono.justOrEmpty(ships.get(characterName));
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
        ships.values()
                .forEach(this::calculateSpaceShip);
    }

    private void calculateSpaceShip(SpaceShip ship) {
        if (ship.isShooting()) {
            Vector2 translation = ship.getTransform().getTranslation();
            var request = new BulletCreateRequest(ship.getShootAngleRadians(), translation.x, translation.y, BulletType.KINETIC_BULLET);
//            wo.create(request)
//                    .subscribe();
        }
    }
}
