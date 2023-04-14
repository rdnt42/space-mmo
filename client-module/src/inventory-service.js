import {Inventory} from "./obj/Inventory.js";
import {EquipmentType} from "./const/EquipmentType.js";

export function initInventory() {
    let inventory = new Inventory();
    inventory.initInventory();
    inventory.addEquipment(EquipmentType.Engine, 2, 123);

    document.addEventListener("keydown", (event) => {
        if (event.key === "i") {
            inventory.changeState();
        }
    });
}

export function doubleClickCallback(equipment) {
    console.log("clicked id: " + equipment.id);
}