package marowak.dev.service.physic;

import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.BroadphaseCollisionData;
import org.dyn4j.world.listener.CollisionListenerAdapter;

@Slf4j
public class ShipCollisionListener extends CollisionListenerAdapter<Body, BodyFixture> {
    @Override
    public boolean collision(BroadphaseCollisionData<Body, BodyFixture> collision) {
        if (collision.getBody1() instanceof SpaceShipBody ship && collision.getBody2() instanceof BulletBody bullet) {
            log.info("Got contact body id: {}, bullet id: {}", ship, bullet.getId());
        }

        return true;
    }
}
