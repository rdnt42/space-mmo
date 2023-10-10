import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";
import {ItemTypeId} from "./const/ItemTypeId.js";
import {HOLD_STORAGE_ID, HULL_STORAGE_ID} from "./const/Common.js";

let inventory;
let itemsMap = new Map();

export function initInventory(data) {
    inventory = new Inventory(data.config);

    for (const itemSrc of data.items) {
        if (!Object.values(ItemTypeId).includes(itemSrc.typeId)) {
            continue;
        }
        let item = new Item(itemSrc);
        if (inventory.initAndAddItem(item)) {
            itemsMap.set(item.id, item);
        } else {
            console.error(`Error when initAndAddItem item with id: ${item.id}`)
        }
    }
}

export function doubleClickCallback(texture) {
    if (texture.textureParentObj instanceof Item) {
        inventory.useItem(texture.textureParentObj);
    }
}

export function dragEndCallback(texture) {
    inventory.moveItem(texture.textureParentObj);
}

export function getEngine() {
    return inventory.getItem(ItemTypeId.Engine);
}

export function updateItemSlot(updateItem) {
    let item = itemsMap.get(updateItem.id);

    if (updateItem.storageId === HOLD_STORAGE_ID) {
        inventory.updateCargo(item, updateItem.slotId);
    } else if (updateItem.storageId === HULL_STORAGE_ID) {
        inventory.updateEquipmentSlot(item);
    }
}

export function changeInventoryState() {
    return inventory.changeState();
}
