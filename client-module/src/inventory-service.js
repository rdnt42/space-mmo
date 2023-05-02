import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";

let inventory;

export function initInventory(slots, items, cargos) {
    console.log(slots)
    inventory = new Inventory(slots);
    for (const item of items) {
        if (item.equipped) {
            let equipment = new Item(item.slotId, item.itemTypeId, false);
            inventory.equip(equipment);
        }
    }

    for (const cargo of cargos) {
        console.log(cargo)
        // inventory.addCargo(cargo);
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

