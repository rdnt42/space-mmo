import * as renderService from "../render-service.js";
import {CargoCell} from "./CargoCell.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import {EquipmentType} from "../const/EquipmentType.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];
    equipmentSlots = new Map();

    constructor(slots) {
        renderService.createInventory();
        for (let i = 0; i < 6; i++) {
            let holdCell = new CargoCell(undefined, i);
            this.cargoCells.push(holdCell);
        }
        this.loadConfig(slots);
    }

    loadConfig(slots) {
        for (let slot of slots) {
            if (Object.values(EquipmentType).includes(slot)) {
                console.log(`init inventory slot: ${slot}`)
                this.equipmentSlots.set(slot, new EquipmentSlot(slot));
            }
        }
    }

    addCargo(cargo) {
        for (let cargoCell of this.cargoCells) {
            if (cargoCell.cargo === undefined) {
                renderService.addToCargo(cargo, cargoCell, true);
                cargoCell.cargo = cargo;
                break;
            }
        }
    }

    swapEquipment(equipment) {
        //TODO refactor
        if (equipment.isEquipped) {
            this.addCargo(equipment);
            equipment.isEquipped = false;
            return;
        }

        let equipmentSlot = this.equipmentSlots.get(equipment.equipmentType);
        if(equipmentSlot === undefined) return;

        let currEquipped = equipmentSlot.equipment;
        renderService.addToEquipmentSlot(equipment, equipmentSlot);
        equipment.isEquipped = true;
        for (let cargoCell of this.cargoCells) {
            if (cargoCell.cargo === equipment) {
                cargoCell.cargo = undefined;
            }
        }

        if (currEquipped !== undefined) {
            equipment.isEquipped = false;
            this.addCargo(currEquipped);
        }
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderService.changeStateInventory(this.isOpen);
    }
}