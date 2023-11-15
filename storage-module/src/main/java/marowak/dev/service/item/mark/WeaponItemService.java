package marowak.dev.service.item.mark;

import marowak.dev.enums.ItemType;

public interface WeaponItemService extends ItemTypeService {
    default ItemType getItemType() {
        return ItemType.ITEM_TYPE_WEAPON;
    }
}
