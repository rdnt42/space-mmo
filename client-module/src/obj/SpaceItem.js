import * as renderService from "../render/render.js";

export class SpaceItem {
    texture;

    id;
    itemTypeId;
    x;
    y;
    name;
    dsc;

    isUpdated;

    constructor(id, x, y, itemTypeId, dsc, name) {
        this.id = id;
        this.itemTypeId = itemTypeId;
        this.x = x;
        this.y = y;
        this.name = name;
        this.dsc = dsc;
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

    destroyObj() {
        renderService.removeSprite(this.texture);
    }

    getShowInfo() {
        return {
            name: this.name,
            dsc: this.dsc,
        };
    }
}