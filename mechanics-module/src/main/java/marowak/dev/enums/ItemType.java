package marowak.dev.enums;

import lombok.Getter;

@Getter
public enum ItemType {
    ITEM_TYPE_ENGINE(1),
    ITEM_TYPE_HULL(8),
    ITEM_TYPE_WEAPON(9);

    ItemType(int typeId) {
        this.typeId = typeId;
    }

    private final int typeId;
}
