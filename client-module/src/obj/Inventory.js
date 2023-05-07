import {CargoCell} from "./CargoCell.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import * as renderEngine from "../render/render.js";
import * as socket from "../websocket-service.js";
import {CharacterItemRequest} from "../request/CharacterRequest.js";

// TODO naming conventions
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
        if (cell === undefined) {
            alert("Нет свободного места в трюме!")
            return false;
        }

        return this.addCargoBySlot(cargo, cell.idx);
    }

    addCargoBySlot(cargo, slotId) {
        let cell = this.cargoCells[slotId];
        cell.addToCargoCell(cargo);
        socket.sendMessage(new CharacterItemRequest(cargo.id, cargo.slotId));

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

    changeEquipmentSlot(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        let currEquipment = equipmentSlot.getEquipment();

        if (equipment.slotId !== null && currEquipment === undefined) {
            this.#equip(equipment);
        } else if (currEquipment === equipment) {
            this.#unequip(equipment);
        } else {
            this.#swap(equipment, currEquipment);
        }
    }

    #equip(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        this.cargoCells[equipment.typeId].removeFromCargoCell();
        equipmentSlot.addToEquipmentSlot(equipment);
        socket.sendMessage(new CharacterItemRequest(equipment.id, equipment.slotId));
    }

    #swap(eqAdd, eqRemove) {
        let currSlotId = eqAdd.slotId;
        this.#equip(eqAdd);
        this.addCargoBySlot(eqRemove, currSlotId);
    }

    #unequip(equipment) {
        if (this.#getFreeCargoCell() === undefined) {
            alert("В трюме нет места, чтобы снять оборудование!")
            return
        }

        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        let removedEquipment = equipmentSlot.removeFromEquipmentSlot();
        this.addCargo(removedEquipment);
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderEngine.changeStateInventory(this.isOpen);
    }

    getEquipment(typeId) {
        return this.equipmentSlots.get(typeId).getEquipment();
    }
}