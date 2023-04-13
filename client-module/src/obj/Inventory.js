import * as renderService from "../render-service.js";
import {Equipment} from "./Equipment.js";

export class Inventory {
    isOpen = false;
    equipments = [];

    constructor(){
    }

    initInventory() {
        renderService.createInventory();
    }

    addEquipment(equipmentType, idx) {
        let equipment = new Equipment();
        equipment.initEquipment(equipmentType, idx);
        this.equipments.push(equipment);

    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderService.changeStateInventory(this.isOpen);
    }
}