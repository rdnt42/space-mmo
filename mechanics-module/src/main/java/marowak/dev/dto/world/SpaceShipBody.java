package marowak.dev.dto.world;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.enums.ForceType;
import org.dyn4j.geometry.Vector2;

@Slf4j
@Getter
@Setter
public class SpaceShipBody extends IdentifiablePhysicalBody {
    public SpaceShipBody(String id) {
        super(id, id);
    }

    public void updatePosition(float speed, float angle, int forceType) {
        log.info("speed: {}, angle: {}, forceType: {}", speed, angle, forceType);
        double angleInRadians = Math.toRadians(angle);

        this.getTransform().setRotation(angleInRadians);
        if (ForceType.POSITIVE.equalsId(forceType)) {
            Vector2 r = new Vector2(this.getTransform().getRotationAngle());
            Vector2 f = r.product(5000.0 * speed);
            this.applyForce(f);
        } else if (ForceType.NEGATIVE.equalsId(forceType)) {
            Vector2 r = new Vector2(this.getTransform().getRotationAngle());
            Vector2 f = r.product(-5000.0 * speed);
            this.applyForce(f);
        } else if (ForceType.REVERSE.equalsId(forceType)) {
            Vector2 r = this.getLinearVelocity();
            Vector2 f = r.product(-15000);
            this.applyForce(f);
        }
        this.setAtRest(false);
    }

}
