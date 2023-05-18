import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";
import {EquipmentSlotId} from "./const/EquipmentSlotId.js";

let inventory;
let itemsMap = new Map();

export function initInventory(response) {
    inventory = new Inventory(response.config);

    for (const itemSrc of response.items) {
        if (!Object.values(EquipmentSlotId).includes(itemSrc.typeId)) {
            continue;
        }
        let item = new Item(itemSrc);
        if (inventory.addInitItem(item)) {
            itemsMap.set(item.id, item);
        }
    }

    document.addEventListener("keydown", (event) => {
        event.preventDefault();
        if (event.key === "i") {
            inventory.changeState();
        }
    });
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
    return inventory.getItem(EquipmentSlotId.Engine);
}

export function updateItemSlot(updateItem) {
    let item = itemsMap.get(updateItem.id);

    if (updateItem.slotId !== null) {
        inventory.updateCargo(item, updateItem.slotId);
    } else {
        inventory.updateEquipmentSlot(item);
    }
}
