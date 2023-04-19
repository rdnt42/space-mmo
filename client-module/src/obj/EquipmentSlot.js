import {renderEngine} from "../render-engine.js";

export class EquipmentSlot {
    texture;
    #equipment;

    constructor(equipmentType) {
        this.texture = renderEngine.initEquipmentSlot(equipmentType);
    }

    addToEquipmentSlot(equipment) {
        this.#equipment = equipment;
        equipment.isEquipped = true;
        renderEngine.addToEquipmentSlot(equipment.texture, this.texture);
    }

    removeFromEquipmentSlot() {
        if(this.#equipment === undefined) return undefined;

        let removedEquipment = this.#equipment;
        removedEquipment.isEquipped = false;
        this.#equipment = undefined;
        renderEngine.removeFromEquipmentSlot(removedEquipment.texture);

        return removedEquipment;
    }
}