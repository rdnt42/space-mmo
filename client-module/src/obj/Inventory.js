import {CargoCell} from "./CargoCell.js";
import {ItemTypeId} from "../const/ItemTypeId.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import * as renderEngine from "../render/render.js";
import * as socket from "../websocket-service.js";
import {CharacterItemRequest} from "../message/CharacterMessage.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];
    equipmentSlots = new Map();

    constructor(config) {
        renderEngine.createInventory();
        for (let i = 0; i < 6; i++) {
            let holdCell = new CargoCell(null, i);
            this.cargoCells.push(holdCell);
        }

        this.loadConfig(config);
    }

    loadConfig(config) {
        // 0b1111001 where 1/0 - open/close slot
        let cfgArr = Array.from(config.toString(2)).reverse();
        for (let i = 0; i < cfgArr.length; i++) {
            if (cfgArr[i] && Object.values(ItemTypeId).includes(i)) {
                this.equipmentSlots.set(i, new EquipmentSlot(i));
            }
        }
        console.log(`init equipment slots: ${cfgArr}`);
    }

    addInitItem(item) {
        if (item.slotId === null) {
            let slot = this.equipmentSlots.get(item.typeId);
            if (slot === undefined) return false;
            slot.add(item);
        } else {
            let cargoCell = this.cargoCells[item.slotId];
            cargoCell.add(item);
        }

        return true;
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
            // TODO rework weapon slots 8-12, but item type just 8
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
        let slot = this.equipmentSlots.get(typeId);
        if (slot === undefined) {
            return null;
        }

        return slot.getItem();
    }
}