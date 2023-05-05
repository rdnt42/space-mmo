import {CargoCell} from "./CargoCell.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import {renderEngine} from "../render/render-engine.js";
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

        cell.addToCargoCell(cargo);
        socket.sendMessage(new CharacterItemRequest(cargo.id, cargo.slotId))

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
        if (equipment.slotId === null) {
            this.#unequip(equipment);
        } else {
            this.#equip(equipment);
        }
    }

    // #64 TODO sending in one place
    #equip(equipment) {
        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        let cell = this.#getCargoCell(equipment);

        let removedFromSlot = equipmentSlot.removeFromEquipmentSlot();
        let removedFromCell = cell.removeFromCargoCell();
        socket.sendMessage(new CharacterItemRequest(equipment.id, equipment.slotId));

        equipmentSlot.addToEquipmentSlot(removedFromCell);
        if (removedFromSlot !== undefined) {
            cell.addToCargoCell(removedFromSlot);
            socket.sendMessage(new CharacterItemRequest(removedFromSlot.id, removedFromSlot.slotId))
        }
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