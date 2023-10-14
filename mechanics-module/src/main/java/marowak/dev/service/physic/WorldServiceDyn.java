package marowak.dev.service.physic;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Value;
import io.micronaut.scheduling.annotation.Async;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.dto.world.KineticBullet;
import marowak.dev.dto.world.SpaceShip;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.World;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Primary
@Singleton
public class WorldServiceDyn implements WorldService {

    @Value("${world.speed.limit}")
    private int speedLimit;
    private World<Body> world;

    private final Map<String, SpaceShip> ships = new ConcurrentHashMap<>();
    private final Map<String, IdentifiablePhysicalBody> bullets = new ConcurrentHashMap<>();


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
    public void createBody(Body body) {
        world.addBody(body);
        switch (body) {
            case SpaceShip ship -> ships.put(ship.getId(), ship);
            case KineticBullet bullet -> bullets.put(bullet.getId(), bullet);
            default -> throw new IllegalStateException("Unexpected value: " + body);
        }
    }

    @Override
    public boolean removeBody(Body body) {
        return world.removeBody(body);
    }

    @Override
    public <T extends Body> List<Body> getBodies(Class<T> tClass) {
        return null;
    }

    @Override
    public <T extends Body> Body getBody(Class<T> tClass, String id) {
        return null;
    }

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
//            createKineticBullet(ship.getShootAngleRadians(), translation.x, translation.y);
        }
    }

    private void calculateBullet(KineticBullet bullet) {
        if (bullet.isAtRest()) {
            boolean removed = world.removeBody(bullet);
            if (removed) {
//                bullets.remove(bullet.getId());
            }
        }
    }
}
