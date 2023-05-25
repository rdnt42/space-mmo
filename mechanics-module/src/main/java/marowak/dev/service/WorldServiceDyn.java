package marowak.dev.service;

import io.micronaut.context.annotation.Primary;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.enums.ForceType;
import marowak.dev.request.CharacterMotionRequest;
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

@Slf4j
@Primary
@Singleton
public class WorldServiceDyn implements WorldService {
    private final World<Body> world;
    private final Map<String, Body> ships = new ConcurrentHashMap<>();

    public WorldServiceDyn() {
        world = new World<>();
        world.setGravity(PhysicsWorld.ZERO_GRAVITY);
    }

    @Override
    public void updateWorld() {
        world.step(1);
    }

    @Override
    public void addShip(CharacterMotion motion) {
        Body body = new Body();

//        BodyFixture bodyFixture = body.addFixture(Geometry.createPolygon(PolygonShapeCfg.getPolygonsDyn(1)));
        BodyFixture bodyFixture = body.addFixture(Geometry.createCircle(64));
        bodyFixture.setDensity(1);
        bodyFixture.setFriction(0.1);
        bodyFixture.setRestitution(0.3);
        bodyFixture.setRestitutionVelocity(0.001);
        body.setLinearDamping(0.1);
        body.setMass(MassType.NORMAL);
        body.translate(motion.x(), motion.y());
        double angleInRadians = Math.toRadians(motion.angle());
        body.getTransform().setRotation(angleInRadians);
        body.setUserData(motion.speed());

        ships.put(motion.characterName(), body);
        world.addBody(body);
    }

    @Override
    public void updateShip(CharacterMotionRequest request, String characterName) {
        Body body = ships.get(characterName);
        double angleInRadians = Math.toRadians(request.angle());

        body.setAtRest(false);
        body.getTransform().setRotation(angleInRadians);
        if (ForceType.POSITIVE.getId() == request.forceTypeId()) {
            Vector2 r = new Vector2(body.getTransform().getRotationAngle());
            Vector2 f = r.product(5000000);
            body.applyForce(f);
        } else if (ForceType.NEGATIVE.getId() == request.forceTypeId()) {
            Vector2 r = new Vector2(body.getTransform().getRotationAngle());
            Vector2 f = r.product(-5000000);
            body.applyForce(f);
        }
    }

    // TODO in range
    @Override
    public Flux<CharacterMotion> getShips(String characterName) {
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

    private double getXShift(float speed, double angle) {
        return Math.cos(angle) * speed;
    }

    private double getYShift(float speed, double angle) {
        return Math.sin(angle) * speed;
    }

    private float getSpeed(Vector2 vector, double rotationAngle) {
        if (Math.signum(vector.getDirection()) == Math.signum(rotationAngle)) {
            return (float) vector.getMagnitude();
        }

        return (float) vector.getMagnitude() * -1f;
    }
}
