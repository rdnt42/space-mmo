import * as renderService from "../render-service.js";

export class Equipment {
    texture;

    idx;
    isEquipped;
    equipmentType;

    constructor(equipmentType, idx, isEquipped) {
        this.isEquipped = isEquipped;
        this.equipmentType = equipmentType;
        this.idx = idx;
        this.texture = renderService.initEquipment(this.equipmentType, this.idx);
        this.texture['textureParentObj'] = this;
    }
}