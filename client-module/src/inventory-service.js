import {Inventory} from "./obj/Inventory.js";
import {Equipment} from "./obj/Equipment.js";

let inventory;

export function initInventory(slots, equipments, cargos) {
    console.log(slots)
    inventory = new Inventory(slots);
    for (const eq of equipments) {
        if (eq.equipped) {
            let equipment = new Equipment(eq.slotId, eq.equipmentType, false);
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
    if (texture.textureParentObj instanceof Equipment) {
        inventory.swapEquipment(texture.textureParentObj);
    }
}

