package marowak.dev.service.physic;

import marowak.dev.dto.bullet.DamageCreator;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.BroadphaseCollisionData;
import org.dyn4j.world.listener.CollisionListenerAdapter;


public class ShipCollisionListener extends CollisionListenerAdapter<Body, BodyFixture> {
    @Override
    public boolean collision(BroadphaseCollisionData<Body, BodyFixture> collision) {
        BulletBody bullet;
        SpaceShipBody ship;

        if (collision.getBody1() instanceof BulletBody bulletBody && collision.getBody2() instanceof SpaceShipBody spaceShipBody) {
            bullet = bulletBody;
            ship = spaceShipBody;
        } else if (collision.getBody2() instanceof BulletBody bulletBody && collision.getBody1() instanceof SpaceShipBody spaceShipBody) {
            bullet = bulletBody;
            ship = spaceShipBody;
        } else return true;

        if (!bullet.getCreatorId().equals(ship.getId())) {
            ship.addDamage(new DamageCreator(bullet.getCreatorId(), bullet.getDamage()));
            bullet.setEnabled(false);
        }

        return true;
    }

}
