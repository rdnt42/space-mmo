package marowak.dev.dto.world;

import lombok.Getter;
import marowak.dev.dto.Point;
import marowak.dev.service.physic.Utils;
import org.dyn4j.dynamics.Body;

@Getter
public abstract class IdentifiablePhysicalBody extends Body {
    private final String id;
    private final String creatorId;

    private boolean needDestroy;

    protected IdentifiablePhysicalBody(String id, String creatorId) {
        this.id = id;
        this.creatorId = creatorId;
        this.needDestroy = false;
    }

    public Point getCoords() {
        return new Point(this.getTransform().getTranslation().x, this.getTransform().getTranslation().y);
    }

    public int getAngle() {
        return (int) Math.toDegrees(this.getTransform().getRotationAngle());
    }

    public float getSpeed() {
        var linearVelocity = this.getLinearVelocity();
        var angle = this.getTransform().getRotationAngle();

        if (Math.signum(linearVelocity.getDirection()) == Math.signum(angle)) {
            return (float) linearVelocity.getMagnitude();
        }

        return (float) linearVelocity.getMagnitude() * -1f;
    }

    public boolean isInRange(Body other) {
        return Utils.isInRange(this.getTransform().getTranslation(), other.getTransform().getTranslation());
    }

    public boolean getNeedDestroy() {
        return needDestroy;
    }

    public void setNeedDestroy(boolean needDestroy) {
        this.needDestroy = needDestroy;
    }

}
