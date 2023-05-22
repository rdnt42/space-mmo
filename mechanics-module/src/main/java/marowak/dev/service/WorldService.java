package marowak.dev.service;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.cfg.PolygonShapeCfg;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.world.PhysicsWorld;
import org.dyn4j.world.World;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Singleton
public class WorldService {
    private final World<Body> world;
    private final Map<String, Body> ships = new ConcurrentHashMap<>();

    public WorldService() {
        world = new World<>();
        world.setGravity(PhysicsWorld.ZERO_GRAVITY);
    }

    public void updateWorld() {
        world.step(1);
    }

    public void addShip(CharacterMotion motion) {
        Body body = new Body();

        body.addFixture(Geometry.createPolygon(PolygonShapeCfg.getPolygonsDyn(1)));
        body.translate(motion.x(), motion.y());
        double angleInRadians = Math.toRadians(motion.angle());
        body.getTransform().setRotation(angleInRadians);
        body.setMass(MassType.NORMAL);
//        body.setUserData(motion.lastUpdateTime());

        ships.put(motion.characterName(), body);
        world.addBody(body);
    }

    public void updateShip(CharacterMotionRequest request, String characterName) {
        Body body = ships.get(characterName);

//        long diffTime = request.lastUpdateTime() - (long) body.getUserData();
//        if (diffTime < 0) return;

        float speed = request.speed();
        double angleInRadians = Math.toRadians(request.angle());
        double xShift = getXShift(speed, angleInRadians);
        double yShift = getYShift(speed, angleInRadians);

        body.setLinearVelocity(xShift, yShift);
        body.getTransform().setRotation(angleInRadians);

//        body.setUserData(request.lastUpdateTime());
    }

    public Flux<CharacterMotion> getShips(String characterName) {
        return Flux.fromStream(ships.entrySet().stream())
                .map(entry -> CharacterMotion.builder()
                        .characterName(entry.getKey())
                        .x(entry.getValue().getTransform().getTranslation().x)
                        .y(entry.getValue().getTransform().getTranslation().y)
                        .angle((int) Math.toDegrees(entry.getValue().getTransform().getRotationAngle()))
                        .speed((float) entry.getValue().getLinearVelocity().getMagnitude())
                        .build());
    }

    public Mono<CharacterMotion> getShip(String characterName) {
        return Mono.justOrEmpty(ships.get(characterName))
                .map(body -> CharacterMotion.builder()
                        .characterName(characterName)
                        .x(body.getTransform().getTranslation().x)
                        .y(body.getTransform().getTranslation().y)
                        .angle((int) Math.toDegrees(body.getTransform().getRotationAngle()))
                        .speed((float) body.getLinearVelocity().getMagnitude())
                        .build());
    }

    private double getXShift(float speed, double angle) {
        return Math.cos(angle) * speed;
    }

    private double getYShift(float speed, double angle) {
        return Math.sin(angle) * speed;
    }
}
