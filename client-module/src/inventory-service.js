import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";
import {EquipmentSlotId} from "./const/EquipmentSlotId.js";

let inventory;

export function initInventory(slots, items) {
    inventory = new Inventory(slots);
    for (const itemSrc of items) {
        let it = new Item(itemSrc);
        inventory.addInitItem(it);
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
        inventory.swapEquipment(texture.textureParentObj);
    }
}

export function getEngine() {
    return inventory.getEquipment(EquipmentSlotId.Engine);
}

