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
                console.log(`init inventory slot: ${slot}`)
                this.equipmentSlots.set(slot, new EquipmentSlot(slot));
            }
        }
    }

    addItem(item) {
        if (item.slotId === null) {
            let slot = this.equipmentSlots.get(item.typeId);
            slot.addToEquipmentSlot(item);
            socket.sendMessage(new CharacterItemRequest(item.id, null))
        } else {
            let cargoCell = this.cargoCells[item.slotId];
            cargoCell.addToCargoCell(item);
            socket.sendMessage(new CharacterItemRequest(item.id, cargoCell.idx))
        }
    }

    addCargo(cargo) {
        let cell = this.#getFreeCargoCell();
        if (cell === undefined) {
            alert("Нет свободного места в трюме!")
            return false;
        }

        cell.addToCargoCell(cargo);
        socket.sendMessage(new CharacterItemRequest(cargo.id, cell.idx))

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
        let equipmentSlot = this.equipmentSlots.get(equipment.typeId);
        let cell = this.#getCargoCell(equipment);

        let removedFromSlot = equipmentSlot.removeFromEquipmentSlot();
        let removedFromCell = cell.removeFromCargoCell();

        equipmentSlot.addToEquipmentSlot(removedFromCell);
        if (removedFromSlot !== undefined) {
            cell.addToCargoCell(removedFromSlot);
            socket.sendMessage(new CharacterItemRequest(removedFromSlot.id, cell.idx))
        }
        socket.sendMessage(new CharacterItemRequest(equipment.id, null))
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
}