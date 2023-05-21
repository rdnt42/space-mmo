package marowak.dev.service;

import jakarta.inject.Singleton;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.request.CharacterMotionRequest;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class WorldServiceJ {
    private final World world;
    private final Map<String, Body> ships = new ConcurrentHashMap<>();

    private final int VELOCITY_ITERATIONS = 6;
    private final int POSITION_ITERATIONS = 2;
    private final float TIME_STEP = 1.0f / 60.0f;


    public WorldServiceJ() {
        world = new World(new Vec2());
    }

    public void updateWorld() {
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
    }

    public void addShip(CharacterMotion motion) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((float) motion.x(), (float) motion.y());
        bodyDef.angle = (float) Math.toRadians(motion.angle());
        bodyDef.type = BodyType.DYNAMIC;

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(100, 100);

        FixtureDef def = new FixtureDef();
        def.shape = polygonShape;
        def.density = 1;
        def.friction = 0.3f;
        body.setUserData(motion.lastUpdateTime());

        body.createFixture(def);
        ships.put(motion.characterName(), body);
    }

    public void updateShip(CharacterMotionRequest request, String characterName) {
        Body body = ships.get(characterName);

        long diffTime = request.lastUpdateTime() - (long) body.getUserData();
        if (diffTime < 0) return;

        float speed = request.speed();
        float angleInRadians = (float) Math.toRadians(request.angle());
        float xShift = getXShift(speed, angleInRadians);
        float yShift = getYShift(speed, angleInRadians);

        body.setLinearVelocity(new Vec2(xShift, yShift));
        body.setTransform(body.getPosition(), angleInRadians);

        body.setUserData(request.lastUpdateTime());
    }

    public Flux<CharacterMotion> getShips(String characterName) {
        return Flux.fromStream(ships.entrySet().stream())
                .map(entry -> CharacterMotion.builder()
                        .characterName(entry.getKey())
                        .x(entry.getValue().getTransform().p.x)
                        .y(entry.getValue().getTransform().p.y)
                        .angle((int) Math.toDegrees(entry.getValue().getTransform().q.getAngle()))
                        .speed((float) Math.hypot(entry.getValue().getLinearVelocity().x, entry.getValue().getLinearVelocity().y))
                        .build());
    }

    public Mono<CharacterMotion> getShip(String characterName) {
        return Mono.justOrEmpty(ships.get(characterName))
                .map(body -> CharacterMotion.builder()
                        .characterName(characterName)
                        .x(body.getTransform().p.x)
                        .y(body.getTransform().p.y)
                        .angle((int) Math.toDegrees(body.getTransform().q.getAngle()))
                        .speed((float) Math.hypot(body.getLinearVelocity().x, body.getLinearVelocity().y))
                        .build());
    }

    private float getXShift(float speed, float angle) {
        return (float) (Math.cos(angle) * speed);
    }

    private float getYShift(float speed, float angle) {
        return (float) (Math.sin(angle) * speed);
    }

}
