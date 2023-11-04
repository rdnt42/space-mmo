import * as render from "../render/render.js";
import {EquipmentTypeId} from "../const/EquipmentTypeId.js";
import {HULL_STORAGE_ID} from "../const/Common.js";

export class EquipmentSlot {
    static storageId = HULL_STORAGE_ID;
    id;
    texture;
    #equipment = null;

    constructor(slotId) {
        this.id = slotId;
        this.texture = render.initEquipmentSlot(slotId);
    }

    add(equipment) {
        this.#equipment = equipment;
        this.#equipment.slotId = null;
        this.#equipment.slot = this;
        render.addToEquipmentSlot(equipment.texture, this.texture);
        if (this.#equipment.typeId === EquipmentTypeId.Engine) {
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
        if (removedEquipment.typeId === EquipmentTypeId.Engine) {
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