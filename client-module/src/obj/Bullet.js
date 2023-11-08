import * as renderService from "../render/render.js";

export class Bullet {
    texture;
    x;
    y;
    angle;
    isUpdated;

    constructor(x, y, angle, type) {
        this.texture = renderService.createBullet(x, y, angle, type);
        this.updateObj(x, y, angle);
    }

    updateObj(x, y, angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.isUpdated = true;
    }

    renderObj() {
        renderService.renderBullet(this.texture, this.x, this.y, this.angle);
    }

    destroyObj() {
        renderService.removeSprite(this.texture);
    }
}