package marowak.dev.enums;

import lombok.Getter;

@Getter
public enum ItemTypes {
    ITEM_TYPE_ENGINE(1),
    ITEM_TYPE_HULL(8),
    ITEM_TYPE_WEAPON(9);

    ItemTypes(int typeId) {
        this.typeId = typeId;
    }

    private final int typeId;
}
