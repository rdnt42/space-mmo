package marowak.dev.dto.world;

import marowak.dev.enums.PhysicalBodyType;
import org.dyn4j.dynamics.Body;

public class KineticBullet extends Body implements PhysicalBody {
    @Override
    public PhysicalBodyType getBodyType() {
        return PhysicalBodyType.BULLET;
    }
}
