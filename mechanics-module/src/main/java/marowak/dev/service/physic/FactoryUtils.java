package marowak.dev.service.physic;

import marowak.dev.dto.BulletConfig;
import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.motion.CharacterMotion;
import marowak.dev.dto.world.*;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

import java.util.concurrent.atomic.LongAdder;

public class FactoryUtils {
    // TODO create config
    private static final BulletConfig kineticCfg = new BulletConfig(
            5,
            6,
            0.1,
            400,
            70,
            10,
            0.05);

    private static final BulletConfig electricCfg = new BulletConfig(
            10,
            3,
            0.05,
            800,
            90,
            100,
            0.5);

    private static final BulletConfig thermalCfg = new BulletConfig(
            10,
            3,
            0.05,
            600,
            90,
            100,
            0.5);

    private static final LongAdder bulletId = new LongAdder();

    private FactoryUtils() {
    }

    private static String getNewId() {
        bulletId.increment();
        return String.valueOf(bulletId.longValue());
    }

    public static KineticBullet createKineticBullet(BulletCreateRequest request) {
        return updateBulletParams(request, kineticCfg, KineticBullet.class);
    }

    public static ElectricBullet createElectricBullet(BulletCreateRequest request) {
        return updateBulletParams(request, electricCfg, ElectricBullet.class);
    }

    public static ThermalBullet createThermalBullet(BulletCreateRequest request) {
        return updateBulletParams(request, electricCfg, ThermalBullet.class);
    }


    private static <T extends BulletBody> T updateBulletParams(BulletCreateRequest request, BulletConfig cfg, Class<T> tClass) {
        try {
            T bullet = tClass.getConstructor(String.class, String.class)
                    .newInstance(getNewId(), request.creatorId());
            // Material
            BodyFixture bodyFixture = bullet.addFixture(
                    Geometry.createRectangle(cfg.getWidth(), cfg.getHeight()));
            bodyFixture.setDensity(cfg.getDensity());
            bodyFixture.setRestitution(cfg.getRestitution() / 100.0);
            bodyFixture.setRestitutionVelocity(0.001);

            // Coordinates and angle
            bullet.translate(request.coords().x(), request.coords().y());
            bullet.getTransform().setRotation(request.angle());

            // resistance and rest
            bullet.setAtRestDetectionEnabled(true);
            bullet.setMass(MassType.NORMAL);
            bullet.setAngularDamping(cfg.getAngularDamping());
            bullet.setLinearDamping(cfg.getLinearDamping());

            // Force
            Vector2 direction = new Vector2(bullet.getTransform().getRotationAngle());
            Vector2 force = direction.product(50 * cfg.getSpeed() * cfg.getMass());
            bullet.applyForce(force);
            Vector2 impulse = new Vector2(request.impulse().x(), request.impulse().y())
                    .product(cfg.getMass());
            bullet.applyImpulse(impulse);

            return bullet;
        } catch (Exception e) {
            return null;
        }
    }

    public static SpaceShipBody createShip(CharacterMotion motion) {
        SpaceShipBody ship = new SpaceShipBody(motion.characterName());

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
