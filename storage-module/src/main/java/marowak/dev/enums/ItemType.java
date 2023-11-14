package marowak.dev.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemType {
    ITEM_TYPE_ENGINE(1),
    ITEM_TYPE_FUEL_TANK(2),
    ITEM_TYPE_SCANNER(3),
    ITEM_TYPE_RADAR(4),
    ITEM_TYPE_DROID(5),
    ITEM_TYPE_CARGO_HOOK(6),
    ITEM_TYPE_CARGO_SHIELD(7),
    ITEM_TYPE_HULL(8),
    ITEM_TYPE_WEAPON(9);

    private final int typeId;
}
