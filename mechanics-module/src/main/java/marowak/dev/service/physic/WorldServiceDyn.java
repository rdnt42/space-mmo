package marowak.dev.service.physic;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.dto.world.SpaceShipBody;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.BroadphaseCollisionDataFilter;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Primary
@Singleton
public class WorldServiceDyn implements WorldService {

    @Value("${world.speed.limit}")
    private int speedLimit;
    private World<Body> world;

    private final Map<String, SpaceShipBody> ships = new ConcurrentHashMap<>();
    private final Map<String, IdentifiablePhysicalBody> bullets = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        world = new World<>();
        world.setGravity(PhysicsWorld.ZERO_GRAVITY);
        world.getSettings().setMaximumTranslation(speedLimit / 60f); // meters per step

        world.getSettings().setMaximumAtRestLinearVelocity(50);
        world.getSettings().setMaximumAtRestAngularVelocity(0.1);

        // filter self shooting
        BroadphaseCollisionDataFilter<Body, BodyFixture> filter = ((body1, fixture1, body2, fixture2) -> {
            if (body1 instanceof SpaceShipBody && body2 instanceof BulletBody) {
                return !Objects.equals(((SpaceShipBody) body1).getId(), ((BulletBody) body2).getCreatorId());
            } else if (body2 instanceof SpaceShipBody && body1 instanceof BulletBody) {
                return !Objects.equals(((SpaceShipBody) body2).getId(), ((BulletBody) body1).getCreatorId());
            }

            return true;
        });

        world.setBroadphaseCollisionDataFilter(filter);
    }

    @Override
    public void updateWorld() {
        try {
            world.step(1);
        } catch (Exception e) {
            log.error("Error when try to update world", e);
        }
    }

    @Override
    public void createBody(Body body) {
        world.addBody(body);
        switch (body) {
            case SpaceShipBody ship -> ships.put(ship.getId(), ship);
            case BulletBody bullet -> bullets.put(bullet.getId(), bullet);
            default -> throw new IllegalStateException("Unexpected value: " + body);
        }
    }

    @Override
    public void removeBody(Body body) {
        if (world.removeBody(body)) {
            switch (body) {
                case SpaceShipBody ship -> ships.remove(ship.getId());
                case BulletBody bullet -> bullets.remove(bullet.getId());
                default -> throw new IllegalStateException("Unexpected value: " + body);
            }
        }
    }

    @Override
    public <T extends Body> List<T> getBodies(Class<T> tClass) {
        if (tClass == SpaceShipBody.class) {
            return ships.values().stream()
                    .map(tClass::cast)
                    .toList();
        } else if (tClass == BulletBody.class) {
            return bullets.values().stream()
                    .map(tClass::cast)
                    .toList();
        }

        return Collections.emptyList();
    }

    @Override
    public <T extends Body> T getBody(Class<T> tClass, String id) {
        if (tClass == SpaceShipBody.class) {
            return tClass.cast(ships.get(id));
        } else if (tClass == BulletBody.class) {
            return tClass.cast(bullets.get(id));
        }

        return null;
    }

}
