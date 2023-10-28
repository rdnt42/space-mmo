package marowak.dev.service.physic;

import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.world.BulletBody;
import marowak.dev.dto.world.SpaceShipBody;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.world.BroadphaseCollisionData;
import org.dyn4j.world.listener.CollisionListenerAdapter;

import java.util.Objects;

@Slf4j
public class ShipCollisionListener extends CollisionListenerAdapter<Body, BodyFixture> {
    @Override
    public boolean collision(BroadphaseCollisionData<Body, BodyFixture> collision) {
        BulletBody bullet = getHit(collision.getBody1(), collision.getBody2());
        if (bullet != null) {
            bullet.setNeedDestroy(true);
            return false;
        }

        return true;
    }

    private BulletBody getHit(Body body1, Body body2) {
        if (body1 instanceof BulletBody bullet && body2 instanceof SpaceShipBody ship && !Objects.equals(ship.getId(), bullet.getCreatorId())) {
            return bullet;
        } else if (body2 instanceof BulletBody bullet && body1 instanceof SpaceShipBody ship && !Objects.equals(ship.getId(), bullet.getCreatorId())) {
            return bullet;
        }

        return null;
    }
}
