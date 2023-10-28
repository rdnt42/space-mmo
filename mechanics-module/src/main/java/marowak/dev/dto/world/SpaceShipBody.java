package marowak.dev.dto.world;

import lombok.Getter;
import lombok.Setter;
import marowak.dev.dto.bullet.DamageCreator;
import marowak.dev.enums.ForceType;
import org.dyn4j.geometry.Vector2;

import java.util.ArrayDeque;
import java.util.Queue;

@Getter
@Setter
public class SpaceShipBody extends IdentifiablePhysicalBody {

    private Queue<DamageCreator> accumulatedDamage = new ArrayDeque<>();

    public SpaceShipBody(String id) {
        super(id, id);
    }

    public void updatePosition(float speed, float angle, int forceType) {
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

    public void addDamage(DamageCreator damage) {
        accumulatedDamage.add(damage);
    }

    public DamageCreator getDamage() {
        return accumulatedDamage.poll();
    }

}
