package marowak.dev.service.physic;

import marowak.dev.dto.world.IdentifiablePhysicalBody;
import marowak.dev.response.BodyInfo;
import org.dyn4j.geometry.Vector2;

import java.util.function.Function;

public class Utils {

    // TODO radar
    private static final int DOUBLED_PLAYERS_IN_RANGE = 1000 * 1000;

    public static boolean isInRange(Vector2 base, Vector2 target) {
        double diffX = base.x - target.x;
        double diffY = base.y - target.y;

        return (diffX * diffX + diffY * diffY) <= DOUBLED_PLAYERS_IN_RANGE;
    }

    public static final Function<IdentifiablePhysicalBody, BodyInfo> bodyToBodyInfo = body ->
            BodyInfo.builder()
                    .id(body.getId())
                    .x(body.getTransform().getTranslation().x)
                    .y(body.getTransform().getTranslation().y)
                    .angle((int) Math.toDegrees(body.getTransform().getRotationAngle()))
                    .speed(getSpeed(body.getLinearVelocity(), body.getTransform().getRotationAngle()))
                    .build();

    private static float getSpeed(Vector2 vector, double rotationAngle) {
        if (Math.signum(vector.getDirection()) == Math.signum(rotationAngle)) {
            return (float) vector.getMagnitude();
        }

        return (float) vector.getMagnitude() * -1f;
    }
}
