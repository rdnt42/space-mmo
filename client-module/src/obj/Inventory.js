import {CargoCell} from "./CargoCell.js";
import {EquipmentTypeId} from "../const/EquipmentTypeId.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import * as renderEngine from "../render/render.js";
import * as socket from "../websocket-service.js";
import {CharacterItemRequest, GetItemFromSpaceRequest} from "../message/CharacterMessage.js";
import {inCargo, inHull, isWeapon} from "../item-utils.js";
import {SPACE_STORAGE_ID} from "../const/Common.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];
    equipmentSlots = new Map();

    constructor(configRaw) {
        // 0b1111001 where 1/0 - open/close slot
        const rawString = configRaw.toString(2).padStart(13, "0");
        let config = Array.from(rawString).reverse();
        console.log(`raw string: ${rawString}`)
        renderEngine.createInventory();
        for (let i = 0; i < 6; i++) {
            let holdCell = new CargoCell(null, i);
            this.cargoCells.push(holdCell);
        }

        this.loadConfig(config);
    }

    loadConfig(config) {
        for (let i = 0; i < config.length; i++) {
            const slotId = i + 1;
            if (config[i] === "1" && Object.values(EquipmentTypeId).includes(slotId)) {
                this.equipmentSlots.set(slotId, new EquipmentSlot(slotId));
            } else {
                renderEngine.addClosedSlot(slotId);
            }
        }
        console.log(`init equipment slots: ${config}`);
    }

    initAndAddItem(item) {
        if (inHull(item)) {
            let slot = this.equipmentSlots.get(item.slotId);
            if (slot === undefined) return false;

            item.initItem();
            slot.add(item);
        } else if (inCargo(item)) {
            let cargoCell = this.cargoCells[item.slotId];

            item.initItem();
            cargoCell.add(item);
        }

        return true;
    }

    updateCargo(item, newSlotId) {
        let cell = this.cargoCells[newSlotId];
        item.removeFromSlot();
        cell.add(item);
    }

    updateEquipmentSlot(item, slotId) {
        let equipmentSlot = this.equipmentSlots.get(slotId);
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

    #getEquipmentSlot(item) {
        if (isWeapon(item)) {
            let slot = this.#getEquipmentSlotWithState(true);
            if (slot) return slot;

            return this.#getEquipmentSlotWithState(false);
        }

        return this.equipmentSlots.get(item.typeId);
    }

    #getEquipmentSlotWithState(isFree) {
        for (let i = EquipmentTypeId.Weapon1; i <= EquipmentTypeId.Weapon5; i++) {
            let slot = this.equipmentSlots.get(i);
            if (!slot) {
                continue;
            }

            if (isFree && slot.getItem() == null) {
                return slot;
            }

            if (!isFree && slot.getItem()) {
                return slot;
            }
        }
    }

    useItem(equipment) {
        let oldSlot = equipment.slot;
        let newSlot;
        if (equipment.slot instanceof EquipmentSlot) {
            newSlot = this.#getFreeCargoCell();
        } else if (equipment.slot instanceof CargoCell) {
            newSlot = this.#getEquipmentSlot(equipment);
        }

        this.#swapSlots(oldSlot, newSlot);
    }

    moveItem(item) {
        if (renderEngine.hasInventoryCollision(item.texture)) {
            console.log(`item in inventory, id: ${item.id}`)
            let newSlot;
            if ((newSlot = this.#getCollisionCell(item)) !== null ||
                (newSlot = this.#getCollisionEquipmentSlot(item)) !== null) {
                this.#swapSlots(item.slot, newSlot);
            } else {
                item.slot.center();
            }
        } else if (renderEngine.hasSpaceCollision(item.texture)) {
            this.#moveItemToSpace(item);
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
        if (isWeapon(item)) {
            for (let i = EquipmentTypeId.Weapon1; i <= EquipmentTypeId.Weapon5; i++) {
                let slot = this.#getCollisionBySlotId(item, i);
                console.log(slot)

                if (slot) return slot;
            }

            return null;
        }

        return this.#getCollisionBySlotId(item, item.slotId);
    }

    #getCollisionBySlotId(item, defaultSlot) {
        let equipmentSlot = this.equipmentSlots.get(defaultSlot);
        if (equipmentSlot == null) return null;
        if (renderEngine.hasHalfCollision(item.texture, equipmentSlot.texture)) {
            return equipmentSlot;
        }

        return null;
    }

    #addToSlot(slot, item) {
        if (slot instanceof EquipmentSlot && item) {
            console.log(`try add item, to equipment slot, id: ${item.id}, slotId: ${slot.id}`);
            socket.sendMessage(new CharacterItemRequest(item.id, slot.id, EquipmentSlot.storageId));
        } else if (slot instanceof CargoCell) {
            console.log(`try add item, to cargo slot, id: ${item.id}, slotId: ${slot.id}`);
            socket.sendMessage(new CharacterItemRequest(item.id, slot.id, CargoCell.storageId));
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

    #moveItemToSpace(item) {
        console.log(`try move item to space, id: ${item.id}`);
        socket.sendMessage(new CharacterItemRequest(item.id, null, SPACE_STORAGE_ID));
    }

    #validateSLotsSwap(slot1, slot2, eq1, eq2) {
        return (slot1 instanceof CargoCell && slot2 instanceof CargoCell) ||
            ((slot1 instanceof EquipmentSlot || slot2 instanceof EquipmentSlot) &&
                ((eq2 !== null && eq1 !== null && eq1.typeId === eq2.typeId) || eq1 === null || eq2 === null));
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderEngine.changeStateInventory(this.isOpen);

        return this.isOpen;
    }

    getItem(typeId) {
        let slot = this.equipmentSlots.get(typeId);
        if (slot === undefined) {
            return null;
        }

        return slot.getItem();
    }

    pickUpItem(item) {
        console.log(`try to pick up an item from space, id: ${item.id}`);
        socket.sendMessage(new GetItemFromSpaceRequest(item.id));
    }
}