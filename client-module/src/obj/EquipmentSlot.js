import * as render from "../render/render.js";
import {ItemTypeId} from "../const/ItemTypeId.js";

export class EquipmentSlot {
    texture;
    #equipment = null;

    constructor(equipmentType) {
        this.texture = render.initEquipmentSlot(equipmentType);
    }

    add(equipment) {
        this.#equipment = equipment;
        this.#equipment.slotId = null;
        this.#equipment.slot = this;
        render.addToEquipmentSlot(equipment.texture, this.texture);
        if (this.#equipment.typeId === ItemTypeId.Engine) {
            render.setSpeedLabel(this.#equipment.maxSpeed);
        }
    }

    remove() {
        if (this.#equipment === null) return null;

        let removedEquipment = this.#equipment;
        this.#equipment = null;
        removedEquipment.slotId = null;
        removedEquipment.slot = null;
        render.removeFromEquipmentSlot(removedEquipment.texture);
        // TODO maybe move to inventory
        if (removedEquipment.typeId === ItemTypeId.Engine) {
            render.setSpeedLabel(0);
        }

        return removedEquipment;
    }

    getItem() {
        return this.#equipment;
    }

    center() {
        render.addToEquipmentSlot(this.#equipment.texture, this.texture);
    }
}