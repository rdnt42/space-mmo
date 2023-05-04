import {renderEngine} from "../render/render-engine.js";

export class EquipmentSlot {
    texture;
    #equipment;

    constructor(equipmentType) {
        this.texture = renderEngine.initEquipmentSlot(equipmentType);
    }

    addToEquipmentSlot(equipment) {
        this.#equipment = equipment;
        renderEngine.addToEquipmentSlot(equipment.texture, this.texture);
    }

    removeFromEquipmentSlot() {
        if(this.#equipment === undefined) return undefined;

        let removedEquipment = this.#equipment;
        this.#equipment = undefined;
        renderEngine.removeFromEquipmentSlot(removedEquipment.texture);

        return removedEquipment;
    }
}