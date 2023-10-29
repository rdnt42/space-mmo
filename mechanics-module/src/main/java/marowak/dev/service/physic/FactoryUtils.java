package marowak.dev.service.physic;

import marowak.dev.dto.bullet.BulletCreateRequest;
import marowak.dev.dto.config.BulletConfig;
import marowak.dev.dto.config.ShipConfig;
import marowak.dev.dto.ship.ShipCreateRequest;
import marowak.dev.dto.world.*;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Polygon;
import org.dyn4j.geometry.Vector2;

import java.util.concurrent.atomic.LongAdder;

public class FactoryUtils {
    private static final LongAdder bulletId = new LongAdder();

    private FactoryUtils() {
    }

    private static String getNewId() {
        bulletId.increment();
        return String.valueOf(bulletId.longValue());
    }

    public static KineticBullet createKineticBullet(BulletCreateRequest request) {
        return createBullet(request, FactoryConfig.kineticCfg, KineticBullet.class);
    }

    public static ElectricBullet createElectricBullet(BulletCreateRequest request) {
        return createBullet(request, FactoryConfig.electricCfg, ElectricBullet.class);
    }

    public static ThermalBullet createThermalBullet(BulletCreateRequest request) {
        return createBullet(request, FactoryConfig.thermalCfg, ThermalBullet.class);
    }


    private static <T extends BulletBody> T createBullet(BulletCreateRequest request, BulletConfig cfg, Class<T> tClass) {
        try {
            T bullet = tClass.getConstructor(String.class, String.class, int.class)
                    .newInstance(getNewId(), request.creatorId(), request.damage());
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

    public static SpaceShipBody createShip(ShipCreateRequest request) {
        SpaceShipBody ship = new SpaceShipBody(request.id());
        ShipConfig shipConfig = FactoryConfig.shipsCfg.get(request.shipTypeId());
        Vector2[] polygonPoints = shipConfig.polygon().stream()
                .map(p -> new Vector2(p.x() * shipConfig.scale(), p.y() * shipConfig.scale()))
                .toArray(Vector2[]::new);

        Polygon polygon = Geometry.createPolygon(polygonPoints);
        polygon.translate(-polygon.getCenter().x, -polygon.getCenter().y);
        BodyFixture bodyFixture = ship.addFixture(polygon);
        bodyFixture.setDensity(1);
        bodyFixture.setFriction(0.1);
        bodyFixture.setRestitution(0.3);
        bodyFixture.setRestitutionVelocity(0.001);
        ship.setLinearDamping(0.1);
        ship.setMass(MassType.NORMAL);
        ship.translate(request.coords().x(), request.coords().y());
        double angleInRadians = Math.toRadians(request.angle());
        ship.getTransform().setRotation(angleInRadians);
        ship.setAtRestDetectionEnabled(false);

        return ship;
    }
}
