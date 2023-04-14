import * as renderService from "../render-service.js";
import {Equipment} from "./Equipment.js";

export class Inventory {
    isOpen = false;
    equipments = [];

    initInventory() {
        renderService.createInventory();
    }

    addEquipment(equipmentType, idx, id) {
        let equipment = new Equipment(equipmentType, idx, id);
        this.equipments.push(equipment);

    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderService.changeStateInventory(this.isOpen);
    }
}