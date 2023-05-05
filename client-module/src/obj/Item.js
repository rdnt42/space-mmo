import {renderEngine} from "../render/render-engine.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";

export class Item {
    texture;
    id;
    typeId;
    subTypeId;
    slotId;

    constructor(item) {
        this.id = item.id;
        this.typeId = item.itemTypeId;
        this.subTypeId = item.subTypeId;
        this.slotId = item.slotId;
        this.additionalFields(item);
        this.texture = renderEngine.initItem(this.typeId, this.subTypeId);
        this.texture['textureParentObj'] = this;
    }

    additionalFields(item) {
        if (item.itemTypeId === EquipmentSlotId.Engine) {
            this.maxSpeed = item.speed;
        }
    }

}