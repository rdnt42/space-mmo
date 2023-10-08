package marowak.dev.dto.world;

import lombok.Getter;
import lombok.Setter;
import marowak.dev.enums.PhysicalBodyType;
import org.dyn4j.dynamics.Body;

@Getter
@Setter
public class SpaceShip extends Body implements PhysicalBody {
    private boolean isShooting;
    private float shootAngleRadians;

    @Override
    public PhysicalBodyType getBodyType() {
        return PhysicalBodyType.SPACE_SHIP;
    }
}
