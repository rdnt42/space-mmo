package marowak.dev.dto.world;

import marowak.dev.enums.BulletType;

public class ElectricBullet extends BulletBody {
    public ElectricBullet(String id, String creatorId, int damage) {
        super(id, creatorId, damage);
    }

    @Override
    public BulletType getType() {
        return BulletType.ELECTRIC;
    }
}
