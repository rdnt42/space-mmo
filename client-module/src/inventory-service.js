import {Inventory} from "./obj/Inventory.js";
import {EquipmentType} from "./const/EquipmentType.js";
import {Equipment} from "./obj/Equipment.js";

let inventory;
export function initInventory() {
    let slots = [EquipmentType.Engine, EquipmentType.FuelTank];
    inventory = new Inventory(slots);
    let engine = new Equipment(EquipmentType.Engine,123, false);
    let fuelTank = new Equipment(EquipmentType.FuelTank,200, false);
    let engineSecond = new Equipment(EquipmentType.Engine,124, false);


    inventory.addCargo(engine);
    inventory.addCargo(fuelTank);
    inventory.addCargo(engineSecond);

    document.addEventListener("keydown", (event) => {
        if (event.key === "i") {
            inventory.changeState();
        }
    });
}

export function doubleClickCallback(item) {
    if (item instanceof Equipment) {
        inventory.swapEquipment(item);
    }
}

