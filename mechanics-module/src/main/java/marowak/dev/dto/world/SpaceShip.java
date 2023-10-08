package marowak.dev.dto.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceShip extends IdentifiablePhysicalBody {
    private boolean isShooting;
    private float shootAngleRadians;

    public SpaceShip(String id) {
        super(id);
    }
}
