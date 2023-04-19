import * as renderService from "../render-service.js";

export class EquipmentSlot {
    texture;
    #equipment;

    constructor(equipmentType) {
        this.texture = renderService.initEquipmentSlot(equipmentType);
    }

    addToEquipmentSlot(equipment) {
        this.#equipment = equipment;
        equipment.isEquipped = true;
        renderService.addToEquipmentSlot(equipment.texture, this.texture);
    }

    removeFromEquipmentSlot() {
        if(this.#equipment === undefined) return undefined;

        let removedEquipment = this.#equipment;
        removedEquipment.isEquipped = false;
        this.#equipment = undefined;
        renderService.removeFromEquipmentSlot(removedEquipment.texture);

        return removedEquipment;
    }
}