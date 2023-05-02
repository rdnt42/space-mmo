import {renderEngine} from "../render/render-engine.js";

export class Item {
    texture;

    isEquipped;
    itemTypeId;
    slotId;

    constructor(slotId, itemTypeId, isEquipped) {
        this.isEquipped = isEquipped;
        this.itemTypeId = itemTypeId;
        this.slotId = slotId;
        this.texture = renderEngine.initItem(this.slotId, this.itemTypeId);
        this.texture['textureParentObj'] = this;
    }
}