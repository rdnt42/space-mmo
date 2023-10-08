package marowak.dev.service.world;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Async;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.dto.world.KineticBullet;
import marowak.dev.dto.world.SpaceShip;
import marowak.dev.enums.ForceType;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import marowak.dev.service.item.ItemService;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.World;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
@Primary
@Singleton
public class WorldServiceDyn implements WorldService {

    @Value("${world.speed.limit}")
    private int speedLimit;

    private static final int DOUBLED_PLAYERS_IN_RANGE = 1000 * 1000;

    private final ItemService itemService;

    private World<Body> world;
    private final Map<String, SpaceShip> ships = new ConcurrentHashMap<>();

    private final LongAdder bulletId = new LongAdder();
    private final Map<String, Body> bullets = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        world = new World<>();
        world.setGravity(PhysicsWorld.ZERO_GRAVITY);
        world.getSettings().setMaximumTranslation(speedLimit / 60f); // meters per step

        world.getSettings().setMaximumAtRestLinearVelocity(50);
        world.getSettings().setMaximumAtRestAngularVelocity(0.1);
    }

    @Override
    public void updateWorld() {
        try {
            world.step(1);
        } catch (Exception e) {
            log.error("Error when try to update world", e);
        }
    }

    @Async
    @Override
    public void calculateObjects() {
        try {
            world.getBodies().stream()
                    .filter(IdentifiablePhysicalBody.class::isInstance)
                    .map(IdentifiablePhysicalBody.class::cast)
                    .toList()
                    .forEach(this::calculateObject);
        } catch (Exception e) {
            log.error("Error when try to calculate objects", e);
        }

    }

    @Override
    public void addShip(CharacterMotion motion) {
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

        world.addBody(ship);
        ships.put(ship.getId(), ship);
    }

    @Override
    public void updateShooting(CharacterShootingRequest request, String characterName) {
        SpaceShip ship = ships.get(characterName);
        ship.setShooting(request.isShooting());
        double angleInRadians = Math.toRadians(request.angle());
        ship.setShootAngleRadians((float) angleInRadians);
    }

    @Override
    public void updateShip(CharacterMotionRequest request, String characterName) {
        SpaceShip ship = ships.get(characterName);

        itemService.getItem(characterName, ItemTypes.ITEM_TYPE_ENGINE)
                .map(Engine.class::cast)
                .flatMap(engine -> applyForce(ship, engine.getSpeed(), request.angle(), request.forceTypeId()))
                .subscribe();
    }

    @Override
    public void deleteShip(String characterName) {
        SpaceShip ship = ships.remove(characterName);
        if (ship != null) {
            world.removeBody(ship);
        }
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

    @Override
    public Flux<BodyInfo> getShipsInRange(String characterName) {
        Vector2 base = ships.get(characterName).getTransform().getTranslation();

        return Flux.fromStream(ships.entrySet().stream())
                .filter(target -> isInRange(base, target.getValue().getTransform().getTranslation()))
                .map(entry -> bodyToBodyInfo.apply(entry.getValue(), entry.getKey()));
    }

    @Override
    public Flux<BodyInfo> getAllShips() {
        return Flux.fromStream(ships.entrySet().stream())
                .map(entry -> bodyToBodyInfo.apply(entry.getValue(), entry.getKey()));
    }

    @Override
    public Flux<BodyInfo> getBulletsInRange(String characterName) {
        Vector2 base = ships.get(characterName).getTransform().getTranslation();

        return Flux.fromStream(bullets.entrySet().stream())
                .filter(entry -> isInRange(base, entry.getValue().getTransform().getTranslation()))
                .map(entry -> bodyToBodyInfo.apply(entry.getValue(), entry.getKey()));
    }

    @Override
    public Mono<BodyInfo> getShip(String characterName) {
        return Mono.justOrEmpty(ships.get(characterName))
                .map(ship -> bodyToBodyInfo.apply(ship, characterName));
    }

    private float getSpeed(Vector2 vector, double rotationAngle) {
        if (Math.signum(vector.getDirection()) == Math.signum(rotationAngle)) {
            return (float) vector.getMagnitude();
        }

        return (float) vector.getMagnitude() * -1f;
    }

    private boolean isInRange(Vector2 base, Vector2 target) {
        double diffX = base.x - target.x;
        double diffY = base.y - target.y;

        return (diffX * diffX + diffY * diffY) <= DOUBLED_PLAYERS_IN_RANGE;
    }

    private void createKineticBullet(double angle, double x, double y) {
        bulletId.increment();
        long id = bulletId.longValue();
        KineticBullet bullet = new KineticBullet(String.valueOf(id));

        // Material
        BodyFixture bodyFixture = bullet.addFixture(Geometry.createRectangle(5, 2));
        bodyFixture.setDensity(0.1);
        bodyFixture.setFriction(0.01);
        bodyFixture.setRestitution(0.7);
        bodyFixture.setRestitutionVelocity(0.001);

        // Coordinates and angle
        bullet.translate(x, y);
        bullet.getTransform().setRotation(angle);

        // resistance and rest
        bullet.setAtRestDetectionEnabled(true);
        bullet.setMass(MassType.NORMAL);
        bullet.setAngularDamping(10);
        bullet.setLinearDamping(0.05);

        // Force
        Vector2 direction = new Vector2(bullet.getTransform().getRotationAngle());
        Vector2 force = direction.product(20000);
        bullet.applyForce(force);

        // init
        world.addBody(bullet);
        bullets.put(bullet.getId(), bullet);
    }

    private final BiFunction<Body, String, BodyInfo> bodyToBodyInfo = (body, id) ->
            BodyInfo.builder()
                    .id(id)
                    .x(body.getTransform().getTranslation().x)
                    .y(body.getTransform().getTranslation().y)
                    .angle((int) Math.toDegrees(body.getTransform().getRotationAngle()))
                    .speed(getSpeed(body.getLinearVelocity(), body.getTransform().getRotationAngle()))
                    .build();

    private void calculateObject(IdentifiablePhysicalBody body) {
        switch (body) {
            case SpaceShip ship -> calculateSpaceShip(ship);
            case KineticBullet bullet -> calculateBullet(bullet);
            default -> log.warn("Unexpected value when calculating objects: {}", body.getClass());
        }
    }

    private void calculateSpaceShip(SpaceShip ship) {
        if (ship.isShooting()) {
            Vector2 translation = ship.getTransform().getTranslation();
            createKineticBullet(ship.getShootAngleRadians(), translation.x, translation.y);
        }
    }

    private void calculateBullet(KineticBullet bullet) {
        if (bullet.isAtRest()) {
            boolean removed = world.removeBody(bullet);
            if (removed) {
                bullets.remove(bullet.getId());
            }
        }
    }
}
