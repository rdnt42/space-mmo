package marowak.dev.enums;

import lombok.Getter;

@Getter
public enum ItemTypes {
    ITEM_TYPE_HULL(8);

    ItemTypes(int typeId) {
        this.typeId = typeId;
    }

    private final int typeId;
}
