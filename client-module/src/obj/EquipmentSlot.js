import * as renderEngine from "../render/render.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";

export class EquipmentSlot {
    texture;
    #equipment;

    constructor(equipmentType) {
        this.texture = renderEngine.initEquipmentSlot(equipmentType);
    }

    addToEquipmentSlot(equipment) {
        this.#equipment = equipment;
        renderEngine.addToEquipmentSlot(equipment.texture, this.texture);
        if (this.#equipment.typeId === EquipmentSlotId.Engine) {
            renderEngine.setSpeedLabel(this.#equipment.maxSpeed);
        }
    }

    removeFromEquipmentSlot() {
        if (this.#equipment === undefined) return undefined;

        let removedEquipment = this.#equipment;
        this.#equipment = undefined;
        renderEngine.removeFromEquipmentSlot(removedEquipment.texture);
        // TODO maybe move to inventory
        if (removedEquipment.typeId === EquipmentSlotId.Engine) {
            renderEngine.setSpeedLabel(0);
        }

        return removedEquipment;
    }

    getEquipment() {
        return this.#equipment;
    }
}