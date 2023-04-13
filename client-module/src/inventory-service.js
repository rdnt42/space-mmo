import {Inventory} from "./obj/Inventory.js";
import {EquipmentType} from "./const/EquipmentType.js";

export function initInventory() {
    let inventory = new Inventory();
    inventory.initInventory();
    inventory.addEquipment(EquipmentType.Engine, 3);

    document.addEventListener("keydown", (event) => {
        if (event.key === "i") {
            inventory.changeState();
        }
    });
}
