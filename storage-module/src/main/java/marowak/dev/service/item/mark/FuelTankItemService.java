package marowak.dev.service.item.mark;

import marowak.dev.enums.ItemType;

public interface FuelTankItemService extends ItemTypeService {
    default ItemType getItemType() {
        return ItemType.ITEM_TYPE_FUEL_TANK;
    }
}
