import {renderEngine} from "../render/render-engine.js";

export class Equipment {
    texture;

    idx;
    isEquipped;
    equipmentType;

    constructor(equipmentType, idx, isEquipped) {
        this.isEquipped = isEquipped;
        this.equipmentType = equipmentType;
        this.idx = idx;
        this.texture = renderEngine.initEquipment(this.equipmentType, this.idx);
        this.texture['textureParentObj'] = this;
    }
}