import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";
import {EquipmentTypeId} from "./const/EquipmentTypeId.js";
import {EquipmentSlot} from "./obj/EquipmentSlot.js";
import {CargoCell} from "./obj/CargoCell.js";
import {SpaceItem} from "./obj/SpaceItem.js";

let inventory;
let itemsMap = new Map();

export function initInventory(data) {
    inventory = new Inventory(data.config);

    for (const itemSrc of data.items) {
        if (!Object.values(EquipmentTypeId).includes(itemSrc.typeId)) {
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

export function clickCallback(texture) {
    if (texture.textureParentObj instanceof SpaceItem) {
        console.log('take item', texture.textureParentObj);
    }
}

export function dragEndCallback(texture) {
    inventory.moveItem(texture.textureParentObj);
}

export function getEngine() {
    return inventory.getItem(EquipmentTypeId.Engine);
}

export function updateItemSlot(updateItem) {
    console.log("get item update", updateItem);
    let item = itemsMap.get(updateItem.id);

    if (updateItem.storageId === CargoCell.storageId) {
        inventory.updateCargo(item, updateItem.slotId);
    } else if (updateItem.storageId === EquipmentSlot.storageId) {
        inventory.updateEquipmentSlot(item, updateItem.slotId);
    }
}

export function changeInventoryState() {
    return inventory.changeState();
}

