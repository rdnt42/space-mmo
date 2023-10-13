package marowak.dev.service.physic;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.dto.world.KineticBullet;
import marowak.dev.request.CharacterShootingRequest;
import marowak.dev.response.BodyInfo;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@RequiredArgsConstructor
@Singleton
public class WeaponServiceImpl implements WeaponService, Calculable {

    private final WorldService worldService;
    private final ShipService shipService;

    private final LongAdder bulletId = new LongAdder();
    private final Map<String, IdentifiablePhysicalBody> bullets = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> updateShooting(CharacterShootingRequest request, String characterName) {
        return shipService.getShipBody(characterName).mapNotNull(ship -> {
            ship.setShooting(request.isShooting());
            double angleInRadians = Math.toRadians(request.angle());
            ship.setShootAngleRadians((float) angleInRadians);

            return null;
        });
    }

    @Override
    public Flux<BodyInfo> getBulletsInRange(String characterName) {
        return shipService.getShipBody(characterName)
                .map(ship -> ship.getTransform().getTranslation())
                .flatMapMany(base -> Flux.fromStream(bullets.entrySet().stream())
                        .filter(entry -> Utils.isInRange(base, entry.getValue().getTransform().getTranslation()))
                        .map(entry -> Utils.bodyToBodyInfo.apply(entry.getValue(), entry.getKey())));
    }

    @Override
    public Mono<Void> createBullet(BulletCreateRequest request) {
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
        bullet.translate(request.x(), request.y());
        bullet.getTransform().setRotation(request.angle());

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
        worldService.createBody(bullet);
        bullets.put(bullet.getId(), bullet);

        return Mono.empty();
    }

    @Async
    @Override
    public void calculate() {
        bullets.values()
                .forEach(this::calculateBullet);
    }

    private void calculateBullet(IdentifiablePhysicalBody body) {
        if (body.isAtRest()) {
            boolean removed = worldService.removeBody(body);
            if (removed) {
                bullets.remove(body.getId());
            }
        }
    }
}
