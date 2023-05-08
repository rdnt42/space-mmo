import * as renderEngine from "../render/render.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";

export class Item {
    texture;

    id;
    typeId;
    equipmentTypeId;
    slotId;

    constructor(item) {
        this.id = item.id;
        this.typeId = item.typeId;
        this.equipmentTypeId = item.equipmentTypeId;
        this.slotId = item.slotId;
        this.additionalFields(item);
        this.texture = renderEngine.initItem(this.typeId, this.equipmentTypeId);
        this.texture['textureParentObj'] = this;
    }

    additionalFields(item) {
        if (item.typeId === EquipmentSlotId.Engine) {
            this.maxSpeed = item.speed;
        }
    }

}