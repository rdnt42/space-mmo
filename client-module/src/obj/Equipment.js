import {renderEngine} from "../render/render-engine.js";

export class Equipment {
    texture;

    isEquipped;
    equipmentType;
    slotId;

    constructor(slotId, equipmentType, isEquipped) {
        this.isEquipped = isEquipped;
        this.equipmentType = equipmentType;
        this.slotId = slotId;
        this.texture = renderEngine.initEquipment(this.slotId, this.equipmentType);
        this.texture['textureParentObj'] = this;
    }
}