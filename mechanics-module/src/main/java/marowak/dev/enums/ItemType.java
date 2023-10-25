package marowak.dev.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemType {
    ITEM_TYPE_ENGINE(1),
    ITEM_TYPE_HULL(8),
    ITEM_TYPE_WEAPON(9);

    private final int typeId;
}
