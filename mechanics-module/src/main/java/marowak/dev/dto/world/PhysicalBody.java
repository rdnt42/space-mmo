package marowak.dev.dto.world;

import marowak.dev.enums.PhysicalBodyType;

public interface PhysicalBody {

    PhysicalBodyType getBodyType();

    default boolean isType(PhysicalBodyType type) {
        return type.equals(getBodyType());
    }
}
