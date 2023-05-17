import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";
import {EquipmentSlotId} from "./const/EquipmentSlotId.js";

let inventory;
let itemsMap = new Map();

export function initInventory(response) {
    if (inventory === undefined) {
        inventory = new Inventory();
        document.addEventListener("keydown", (event) => {
            event.preventDefault();
            if (event.key === "i") {
                inventory.changeState();
            }
        });
    }
    if (!Object.values(EquipmentSlotId).includes(response.typeId)) {
        return;
    }
    let it = new Item(response);
    inventory.addInitItem(it);
    itemsMap.set(it.id, it);
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
