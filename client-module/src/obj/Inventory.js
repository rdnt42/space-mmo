import {CargoCell} from "./CargoCell.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import {EquipmentType} from "../const/EquipmentType.js";
import {renderEngine} from "../render/render-engine.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];
    equipmentSlots = new Map();

    constructor(slots) {
        renderEngine.createInventory();
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
        let cell = this.#getFreeCargoCell();
        if (cell === undefined) {
            alert("Нет свободного места в трюме!")
            return false;
        }

        cell.addToCargoCell(cargo);
        return true;
    }

    #getCargoCell(equipment) {
        for (let cargoCell of this.cargoCells) {
            if (cargoCell.getCargo() === equipment) {
                return cargoCell;
            }
        }

        return undefined;
    }

    #getFreeCargoCell() {
        return this.#getCargoCell(undefined);
    }

    swapEquipment(equipment) {
        if (equipment.isEquipped) {
            this.#unequip(equipment);
        } else {
            this.#equip(equipment);
        }
    }

    #equip(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.slotId);
        let cell = this.#getCargoCell(equipment);

        let removedFromSlot = equipmentSlot.removeFromEquipmentSlot();
        let removedFromCell = cell.removeFromCargoCell();

        equipmentSlot.addToEquipmentSlot(removedFromCell);
        if (removedFromSlot !== undefined) {
            cell.addToCargoCell(removedFromSlot);
        }
    }

    equip(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.slotId);
        if (equipmentSlot !== undefined) {
            equipmentSlot.addToEquipmentSlot(equipment);
        }
    }

    #unequip(equipment) {
        if (this.#getFreeCargoCell() === undefined) {
            alert("В трюме нет места, чтобы снять оборудование!")
            return
        }

        let equipmentSlot = this.equipmentSlots.get(equipment.slotId);
        let removedEquipment = equipmentSlot.removeFromEquipmentSlot();
        this.addCargo(removedEquipment);
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderEngine.changeStateInventory(this.isOpen);
    }
}