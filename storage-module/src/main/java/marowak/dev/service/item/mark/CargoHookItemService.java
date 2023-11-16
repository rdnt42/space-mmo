package marowak.dev.service.item.mark;

import marowak.dev.enums.ItemType;

public interface CargoHookItemService extends ItemTypeService {
    default ItemType getItemType() {
        return ItemType.ITEM_TYPE_CARGO_HOOK;
    }
}
