package marowak.dev.dto.world;

import marowak.dev.enums.BulletType;

public class KineticBullet extends BulletBody {
    public KineticBullet(String id, String creatorId, int damage) {
        super(id, creatorId, damage);
    }

    @Override
    public BulletType getType() {
        return BulletType.KINETIC;
    }
}
