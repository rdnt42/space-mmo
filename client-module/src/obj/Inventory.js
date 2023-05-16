import {CargoCell} from "./CargoCell.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import * as renderEngine from "../render/render.js";
import * as socket from "../websocket-service.js";
import {CharacterItemRequest} from "../message/CharacterMessage.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];
    equipmentSlots = new Map();

    constructor() {
        renderEngine.createInventory();
        for (let i = 0; i < 6; i++) {
            let holdCell = new CargoCell(null, i);
            this.cargoCells.push(holdCell);
        }
        let slots = []
        slots.push(1, 2, 3, 4, 5, 6, 7, 8);
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
            slot.add(item);
        } else {
            let cargoCell = this.cargoCells[item.slotId];
            cargoCell.add(item);
        }
    }

    updateCargo(item, newSlotId) {
        let cell = this.cargoCells[newSlotId];
        item.removeFromSlot();
        cell.add(item);
    }

    updateEquipmentSlot(item) {
        let equipmentSlot = this.equipmentSlots.get(item.typeId);
        item.removeFromSlot();
        equipmentSlot.add(item);
    }

    #getCargoCell(item) {
        for (let cargoCell of this.cargoCells) {
            if (cargoCell.getItem() === item) {
                return cargoCell;
            }
        }

        return null;
    }

    #getFreeCargoCell() {
        let cell = this.#getCargoCell(null);
        if (cell === null) {
            alert("Нет свободного места в трюме!")
        }

        return cell;
    }

    useItem(equipment) {
        let oldSlot = equipment.slot;
        let newSlot;
        if (equipment.slot instanceof EquipmentSlot) {
            newSlot = this.#getFreeCargoCell();
        } else if (equipment.slot instanceof CargoCell) {
            newSlot = this.equipmentSlots.get(equipment.typeId);
        }

        this.#swapSlots(oldSlot, newSlot);
    }

    moveItem(item) {
        let newSlot;
        if ((newSlot = this.#getCollisionCell(item)) !== null ||
            (newSlot = this.#getCollisionEquipmentSlot(item)) !== null) {
            this.#swapSlots(item.slot, newSlot);
        } else {
            item.slot.center();
        }
    }

    #getCollisionCell(item) {
        for (const cell of this.cargoCells) {
            if (renderEngine.hasHalfCollision(item.texture, cell.texture)) {
                return cell;
            }
        }

        return null;
    }

    #getCollisionEquipmentSlot(item) {
        let equipmentSlot = this.equipmentSlots.get(item.typeId);
        if (renderEngine.hasHalfCollision(item.texture, equipmentSlot.texture)) {
            return equipmentSlot;
        }

        return null;
    }

    #addToSlot(slot, item) {
        if (slot instanceof EquipmentSlot && item) {
            socket.sendMessage(new CharacterItemRequest(item.id, null));
        } else if (slot instanceof CargoCell) {
            socket.sendMessage(new CharacterItemRequest(item.id, slot.idx));
        }
    }

    #swapSlots(slot1, slot2) {
        if (!this.#validateSLotsSwap(slot1, slot2, slot1.getItem(), slot2.getItem())) {
            alert("Недопустимый тип оборудования для замены!")
            slot1.center();
            slot2.center();
            return;
        }
        let item1 = slot1.getItem();
        let item2 = slot2.getItem();
        if (item2 !== null) {
            this.#addToSlot(slot1, item2);
        }
        if (item1 !== null) {
            this.#addToSlot(slot2, item1);
        }
    }

    #validateSLotsSwap(slot1, slot2, eq1, eq2) {
        return (slot1 instanceof CargoCell && slot2 instanceof CargoCell) ||
            ((slot1 instanceof EquipmentSlot || slot2 instanceof EquipmentSlot) &&
                ((eq2 !== null && eq1 !== null && eq1.typeId === eq2.typeId) || eq1 === null || eq2 === null));
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderEngine.changeStateInventory(this.isOpen);
    }

    getItem(typeId) {
        return this.equipmentSlots.get(typeId).getItem();
    }
}