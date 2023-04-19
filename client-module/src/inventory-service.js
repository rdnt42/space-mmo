import {Inventory} from "./obj/Inventory.js";
import {EquipmentType} from "./const/EquipmentType.js";
import {Equipment} from "./obj/Equipment.js";

let inventory;

export function initInventory() {
    let slots = [EquipmentType.Engine, EquipmentType.FuelTank];
    inventory = new Inventory(slots);
    let engine = new Equipment(EquipmentType.Engine, 1, false);
    let fuelTank = new Equipment(EquipmentType.FuelTank, 1, false);
    let engineSecond = new Equipment(EquipmentType.Engine, 2, false);
    let engineThird = new Equipment(EquipmentType.Engine, 3, false);


    inventory.addCargo(engine);
    inventory.addCargo(fuelTank);
    inventory.addCargo(engineSecond);
    inventory.addCargo(engineThird);

    document.addEventListener("keydown", (event) => {
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

