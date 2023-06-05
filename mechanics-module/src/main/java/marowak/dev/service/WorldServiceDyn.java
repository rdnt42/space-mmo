package marowak.dev.service;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Async;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.BodyUserData;
import marowak.dev.dto.world.Bullet;
import marowak.dev.enums.ForceType;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.CharacterMotionRequest;
import marowak.dev.request.CharacterShootingRequest;
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
    private final Map<String, Body> ships = new ConcurrentHashMap<>();

    private final LongAdder bulletId = new LongAdder();
    private final Map<Long, Body> bullets = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        world = new World<>();
        world.setGravity(PhysicsWorld.ZERO_GRAVITY);
        world.getSettings().setMaximumTranslation(speedLimit / 60f); // meters per step
        world.getSettings().setMaximumAtRestLinearVelocity(0.1);
    }

    @Override
    public void updateWorld() {
        world.step(1);
    }

    @Async
    @Override
    public void calculateObjects() {
        for (Body body : ships.values()) {
            BodyUserData data = (BodyUserData) body.getUserData();
            if (data.isShooting()) {
                Vector2 translation = body.getTransform().getTranslation();
                createBullet(data.getShootAngle(), translation.x, translation.y);
            }
        }

        for (Map.Entry<Long, Body> entry : bullets.entrySet()) {
            if (entry.getValue().isAtRest()) {
                bullets.remove(entry.getKey());
                world.removeBody(entry.getValue());
            }
        }
    }

    @Override
    public void addShip(CharacterMotion motion) {
        Body body = new Body();

        BodyFixture bodyFixture = body.addFixture(Geometry.createCircle(64 * 0.75));
        bodyFixture.setDensity(1);
        bodyFixture.setFriction(0.1);
        bodyFixture.setRestitution(0.3);
        bodyFixture.setRestitutionVelocity(0.001);
        body.setLinearDamping(0.1);
        body.setMass(MassType.NORMAL);
        body.translate(motion.x(), motion.y());
        double angleInRadians = Math.toRadians(motion.angle());
        body.getTransform().setRotation(angleInRadians);
        body.setUserData(new BodyUserData());
        body.setAtRestDetectionEnabled(false);

        world.addBody(body);
        ships.put(motion.characterName(), body);
    }

    @Override
    public void updateShooting(CharacterShootingRequest request, String characterName) {
        Body body = ships.get(characterName);
        BodyUserData data = (BodyUserData) body.getUserData();
        data.setShooting(request.isShooting());
        data.setShootAngle(request.angle());
    }

    @Override
    public void updateShip(CharacterMotionRequest request, String characterName) {
        Body body = ships.get(characterName);

        itemService.getItem(characterName, ItemTypes.ITEM_TYPE_ENGINE)
                .flatMap(engine -> updateState(body, (Engine) engine, request.angle(), request.forceTypeId()))
                .subscribe();

    }

    @Override
    public void deleteShip(String characterName) {
        Body body = ships.remove(characterName);
        if (body != null) {
            world.removeBody(body);
        }
    }

    private Mono<Void> updateState(Body body, Engine engine, float angle, int forceType) {
        double angleInRadians = Math.toRadians(angle);

        body.getTransform().setRotation(angleInRadians);
        if (ForceType.POSITIVE.equalsId(forceType)) {
            Vector2 r = new Vector2(body.getTransform().getRotationAngle());
            Vector2 f = r.product(5000.0 * engine.getSpeed());
            body.applyForce(f);
        } else if (ForceType.NEGATIVE.equalsId(forceType)) {
            Vector2 r = new Vector2(body.getTransform().getRotationAngle());
            Vector2 f = r.product(-5000.0 * engine.getSpeed());
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
    public Flux<CharacterMotion> getShipsInRange(String characterName) {
        Vector2 base = ships.get(characterName).getTransform().getTranslation();

        return Flux.fromStream(ships.entrySet().stream())
                .filter(target -> isInRange(base, target.getValue().getTransform().getTranslation()))
                .map(entry -> CharacterMotion.builder()
                        .characterName(entry.getKey())
                        .x(entry.getValue().getTransform().getTranslation().x)
                        .y(entry.getValue().getTransform().getTranslation().y)
                        .angle((int) Math.toDegrees(entry.getValue().getTransform().getRotationAngle()))
                        .speed(getSpeed(entry.getValue().getLinearVelocity(), entry.getValue().getTransform().getRotationAngle()))
                        .build());
    }

    @Override
    public Flux<CharacterMotion> getAllShips() {
        return Flux.fromStream(ships.entrySet().stream())
                .map(entry -> CharacterMotion.builder()
                        .characterName(entry.getKey())
                        .x(entry.getValue().getTransform().getTranslation().x)
                        .y(entry.getValue().getTransform().getTranslation().y)
                        .angle((int) Math.toDegrees(entry.getValue().getTransform().getRotationAngle()))
                        .speed(getSpeed(entry.getValue().getLinearVelocity(), entry.getValue().getTransform().getRotationAngle()))
                        .build());
    }

    @Override
    public Flux<Bullet> getBulletsInRange(String characterName) {
        Vector2 base = ships.get(characterName).getTransform().getTranslation();

        return Flux.fromStream(bullets.entrySet().stream())
                .filter(entry -> isInRange(base, entry.getValue().getTransform().getTranslation()))
                .map(entry -> Bullet.builder()
                        .id(entry.getKey())
                        .x(entry.getValue().getTransform().getTranslation().x)
                        .y(entry.getValue().getTransform().getTranslation().y)
                        .angle(entry.getValue().getTransform().getRotationAngle())
                        .build());
    }

    @Override
    public Mono<CharacterMotion> getShip(String characterName) {
        return Mono.justOrEmpty(ships.get(characterName))
                .map(body -> CharacterMotion.builder()
                        .characterName(characterName)
                        .x(body.getTransform().getTranslation().x)
                        .y(body.getTransform().getTranslation().y)
                        .angle((int) Math.toDegrees(body.getTransform().getRotationAngle()))
                        .speed(getSpeed(body.getLinearVelocity(), body.getTransform().getRotationAngle()))
                        .build());
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

    private void createBullet(double angle, double x, double y) {
        Body body = new Body();

        BodyFixture bodyFixture = body.addFixture(Geometry.createRectangle(5, 2));
        bodyFixture.setDensity(0.1);
        bodyFixture.setFriction(0.01);
        bodyFixture.setRestitution(0.7);
        bodyFixture.setRestitutionVelocity(0.001);
        body.setLinearDamping(0.1);
        body.setMass(MassType.NORMAL);
        body.translate(x, y);
        body.getTransform().setRotation(angle);
        body.setBullet(true);

        Vector2 direction = new Vector2(body.getTransform().getRotationAngle());
        Vector2 force = direction.product(20000);
        body.applyForce(force);
        body.setAtRestDetectionEnabled(true);

        bulletId.increment();
        long id = bulletId.longValue();
        world.addBody(body);
        bullets.put(id, body);
    }
}