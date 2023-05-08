import {CargoCell} from "./CargoCell.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import * as renderEngine from "../render/render.js";

// TODO naming conventions, too lot code
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
            if (Object.values(EquipmentSlotId).includes(slot)) {
                this.equipmentSlots.set(slot, new EquipmentSlot(slot));
            }
        }
        console.log(`init equipment slots: ${slots}`);
    }

    addInitItem(item) {
        if (item.slotId === null) {
            let slot = this.equipmentSlots.get(item.typeId);
            slot.addToEquipmentSlot(item);
        } else {
            let cargoCell = this.cargoCells[item.slotId];
            cargoCell.addToCargoCell(item);
        }
    }

    addCargo(cargo) {
        let cell = this.#getFreeCargoCell();
        if (cell === undefined) return false;

        return this.addCargoBySlot(cargo, cell.idx);
    }

    addCargoBySlot(cargo, slotId) {
        let cell = this.cargoCells[slotId];
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
        let cell = this.#getCargoCell(undefined);
        if (cell === undefined) {
            alert("Нет свободного места в трюме!")
        }

        return cell;
    }

    changeEquipmentSlot(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        let currEquipment = equipmentSlot.getEquipment();

        if (equipment.slotId !== null && currEquipment === undefined) {
            this.#equip(equipment);
        } else if (currEquipment === equipment) {
            let cell = this.#getFreeCargoCell();
            if (cell === undefined) return;

            this.#unequipTo(equipment, cell.idx);
        } else {
            this.#swapEquipment(equipment, currEquipment);
        }
    }

    // TODO too lot branches
    moveItem(item) {
        if (item.slotId === null) { // unequip equipment
            let equipmentSlot = this.equipmentSlots.get(item.typeId);
            let newCell = this.#getCollisionCell(item);

            if (newCell !== undefined && newCell.getCargo() !== undefined) {
                this.#swapEquipment(newCell.getCargo(), item);
            } else if (newCell !== undefined) {
                this.#unequipTo(item, newCell.idx);
            } else {
                equipmentSlot.center();
            }
        } else { // moving in hold
            let oldCell = this.#getCargoCell(item);
            let newCell = this.#getCollisionCell(item);

            let eqSlot;
            if (newCell !== undefined) {
                oldCell.swapCargo(newCell);
            } else if ((eqSlot = this.#getCollisionEquipmentSlot(item)) !== undefined && eqSlot.getEquipment() === undefined) {
                this.#equip(item);
            } else if (eqSlot !== undefined && eqSlot.getEquipment() !== undefined) {
                this.#swapEquipment(item, eqSlot.getEquipment())
            } else {
                oldCell.center();
            }
        }
    }

    #getCollisionCell(item) {
        for (const cell of this.cargoCells) {
            if (renderEngine.hasHalfCollision(item.texture, cell.texture)) {
                return cell;
            }
        }

        return undefined;
    }

    #getCollisionEquipmentSlot(item) {
        let equipmentSlot = this.equipmentSlots.get(item.typeId);
        if (renderEngine.hasHalfCollision(item.texture, equipmentSlot.texture)) {
            return equipmentSlot;
        }

        return undefined;
    }

    #equip(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        this.cargoCells[equipment.slotId].removeFromCargoCell();
        equipmentSlot.addToEquipmentSlot(equipment);
    }

    #swapEquipment(eqAdd, eqRemove) {
        if (eqAdd.typeId !== eqRemove.typeId) {
            alert("Недопустимый тип оборудования для замены!")
        }
        let currSlotId = eqAdd.slotId;
        this.#equip(eqAdd);
        this.addCargoBySlot(eqRemove, currSlotId);
    }

    #unequipTo(equipment, slotId) {
        if (this.cargoCells[slotId].getCargo() !== undefined) {
            alert("Выбранная ячейка занята!");
        }

        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        let removedEquipment = equipmentSlot.removeFromEquipmentSlot();
        this.addCargoBySlot(removedEquipment, slotId);
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderEngine.changeStateInventory(this.isOpen);
    }

    getEquipment(typeId) {
        return this.equipmentSlots.get(typeId).getEquipment();
    }
}