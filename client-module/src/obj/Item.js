import * as renderEngine from "../render/render.js";
import {ItemTypeId} from "../const/ItemTypeId.js";

export class Item {
    texture;

    id;
    typeId;
    equipmentTypeId;
    slotId;
    slot = null;

    constructor(item) {
        this.id = item.id;
        this.typeId = item.typeId;
        this.equipmentTypeId = item.equipmentTypeId;
        this.slotId = item.slotId;
        this.storageId = item.storageId;
        this.additionalFields(item);
        this.texture = renderEngine.initItem(this.typeId, this.equipmentTypeId);
        this.texture['textureParentObj'] = this;
    }

    additionalFields(item) {
        if (item.typeId === ItemTypeId.Engine) {
            this.maxSpeed = item.speed;
        }
    }

    removeFromSlot() {
        if (this.slot !== null && this.slot.getItem() !== null && this.slot.getItem().id === this.id) {
            this.slot.remove();
        }
    }

}