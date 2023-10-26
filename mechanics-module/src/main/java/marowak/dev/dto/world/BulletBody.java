package marowak.dev.dto.world;

import marowak.dev.enums.BulletType;

public abstract class BulletBody extends IdentifiablePhysicalBody {
    private final int damage;

    protected BulletBody(String id, String creatorId, int damage) {
        super(id, creatorId);
        this.damage = damage;
    }

    public abstract BulletType getType();

    public int getDamage() {
        return damage;
    }
}
