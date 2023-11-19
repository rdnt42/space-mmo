import * as renderService from "../render/render.js";

export class SpaceItem {
    texture;

    id;
    itemTypeId;
    dsc;
    x;
    y;

    isUpdated;

    constructor(id, x, y, itemTypeId, dsc) {
        this.id = id;
        this.itemTypeId = itemTypeId;
        this.dsc = dsc;
        this.x = x;
        this.y = y;
        this.texture = renderService.createSpaceItem(x, y, itemTypeId);
        this.texture['textureParentObj'] = this;
    }

    updateObj(x, y) {
        this.x = x;
        this.y = y;
        this.isUpdated = true;
    }

    renderObj() {
        renderService.renderSpaceItem(this.texture, this.x, this.y);
    }

    showInfo() {
        console.log("show item in space info")
    }
}