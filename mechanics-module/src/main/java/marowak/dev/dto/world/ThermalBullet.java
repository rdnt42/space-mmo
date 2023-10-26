package marowak.dev.dto.world;

import marowak.dev.enums.BulletType;

public class ThermalBullet extends BulletBody {
    public ThermalBullet(String id, String creatorId, int damage) {
        super(id, creatorId, damage);
    }

    @Override
    public BulletType getType() {
        return BulletType.THERMAL;
    }
}
