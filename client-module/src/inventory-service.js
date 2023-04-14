import {Inventory} from "./obj/Inventory.js";
import {EquipmentType} from "./const/EquipmentType.js";
import {Equipment} from "./obj/Equipment.js";

export function initInventory() {
    let inventory = new Inventory();
    let engine = new Equipment(EquipmentType.Engine,123, false);
    let fuelTank = new Equipment(EquipmentType.FuelTank,200, false);

    inventory.addCargo(engine);
    inventory.addCargo(fuelTank);

    document.addEventListener("keydown", (event) => {
        if (event.key === "i") {
            inventory.changeState();
        }
    });
}

export function doubleClickCallback(equipment) {
    console.log("clicked id: " + equipment.id);
}