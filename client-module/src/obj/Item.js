import {renderEngine} from "../render/render-engine.js";

export class Item {
    texture;
    id;
    isEquipped;
    typeId;
    subTypeId;
    slotId;

    constructor(id, slotId, typeId, subTypeId, isEquipped) {
        this.id = id;
        this.isEquipped = isEquipped;
        this.typeId = typeId;
        this.subTypeId = subTypeId;
        this.slotId = slotId;
        this.texture = renderEngine.initItem(this.typeId, this.subTypeId);
        this.texture['textureParentObj'] = this;
    }

}