package marowak.dev.dto.world;

import marowak.dev.enums.BulletType;

public abstract class BulletBody extends IdentifiablePhysicalBody {
    protected BulletBody(String id, String creatorId) {
        super(id, creatorId);
    }

    public abstract BulletType getType();
}
