import {renderEngine} from "../render/render-engine.js";

export class Item {
    texture;

    isEquipped;
    typeId;
    subTypeId;
    slotId;

    constructor(slotId, typeId, subTypeId, isEquipped) {
        this.isEquipped = isEquipped;
        this.typeId = typeId;
        this.subTypeId = subTypeId;
        this.slotId = slotId;
        this.texture = renderEngine.initItem(this.typeId, this.subTypeId);
        this.texture['textureParentObj'] = this;
    }

}