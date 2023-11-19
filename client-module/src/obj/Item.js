import * as renderEngine from "../render/render.js";
import {EquipmentTypeId} from "../const/EquipmentTypeId.js";

export class Item {
    texture;

    id;
    typeId;
    equipmentTypeId;
    slotId;
    slot = null;
    dsc;

    constructor(item) {
        this.id = item.id;
        this.typeId = item.typeId;
        this.equipmentTypeId = item.equipmentTypeId;
        this.slotId = item.slotId;
        this.storageId = item.storageId;
        this.dsc = item.dsc;
        this.additionalFields(item);
    }

    initItem() {
        console.log(`init item id: ${this.id}`, {
            typeId: this.typeId,
            equipmentId: this.equipmentTypeId,
            slotId: this.slotId,
            storageId: this.storageId
        });
        this.texture = renderEngine.initItem(this.typeId, this.equipmentTypeId);
        this.texture['textureParentObj'] = this;
    }

    additionalFields(item) {
        if (item.typeId === EquipmentTypeId.Engine) {
            this.maxSpeed = item.speed;
        }
    }

    removeFromSlot() {
        if (this.slot !== null && this.slot.getItem() !== null && this.slot.getItem().id === this.id) {
            this.slot.remove();
        }
    }

    showInfo() {
        return this.dsc;
    }

}