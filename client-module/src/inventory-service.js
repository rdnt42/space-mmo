import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";

let inventory;

export function initInventory(slots, items) {
    console.log(slots)
    inventory = new Inventory(slots);
    for (const item of items) {
        let it = new Item(item.id, item.slotId, item.itemTypeId, item.subTypeId);
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

