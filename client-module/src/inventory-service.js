import {Inventory} from "./obj/Inventory.js";
import {Equipment} from "./obj/Equipment.js";
import {EquipmentType} from "./const/EquipmentType.js";

export function initInventory() {
  let inventory = new Inventory();
  inventory.initInventory();
  let engine = new Equipment();
  engine.initEquipment(EquipmentType.Engine);

  document.addEventListener("keydown", (event) => {
    if (event.key === "i") {
      inventory.changeState();
    }
  });
}
