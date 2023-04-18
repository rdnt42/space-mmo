import * as PIXI from "../libs/pixi.min.js";
import * as renderService from "../render-service.js";

export class EquipmentSlot extends PIXI.Sprite {
    #equipment;

    constructor(equipmentType) {
        super(PIXI.Texture.WHITE);
        renderService.initEquipmentSlot(this, equipmentType);
    }

    addToEquipmentSlot(equipment) {
        this.#equipment = equipment;
        equipment.isEquipped = true;
        renderService.addToEquipmentSlot(equipment, this);
        equipment.visible = true;
    }

    removeFromEquipmentSlot() {
        if(this.#equipment === undefined) return undefined;

        let removedEquipment = this.#equipment;
        removedEquipment.isEquipped = false;
        this.#equipment = undefined;
        removedEquipment.visible = false;

        return removedEquipment;
    }
}