package marowak.dev.service.physic;

import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.KineticBullet;
import marowak.dev.dto.world.SpaceShip;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.concurrent.atomic.LongAdder;

public class FactoryUtils {
    private FactoryUtils() {
    }

    private static final LongAdder bulletId = new LongAdder();

    public static KineticBullet createKineticBullet(BulletCreateRequest request) {
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

        return bullet;
    }

    public static SpaceShip createShip(CharacterMotion motion) {
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

        return ship;
    }
}
