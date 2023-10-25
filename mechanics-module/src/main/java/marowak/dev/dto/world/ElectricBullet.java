package marowak.dev.dto.world;

import marowak.dev.enums.BulletType;

public class ElectricBullet extends BulletBody {
    public ElectricBullet(String id, String creatorId) {
        super(id, creatorId);
    }

    @Override
    public BulletType getType() {
        return BulletType.ELECTRIC;
    }
}
