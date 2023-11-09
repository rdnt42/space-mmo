import * as renderService from "../render/render.js";
import {getRelativeX, getRelativeY} from "../obj-service/obj-utils.js";

export class Ship {
    texture;
    characterName;
    movement;
    isUpdated;

    constructor(characterName, x, y, angle, speed, typeId, polygon) {
        this.texture = renderService.createCharacter(characterName, typeId, x, y, angle, polygon);
        this.characterName = characterName;
        this.movement = {
            x: x,
            y: y,
        }
        this.updateObj(x, y, angle, speed);
    }

    updateObj(x, y, angle, speed) {
        this.movement = {
            prevX: this.movement.x,
            prevY: this.movement.y,
            x: x,
            y: y,
            angle: angle,
            speed: speed,
        }
        this.isUpdated = true;
    }

    renderObj(absX, absY) {
        const relativeX = getRelativeX(this.movement.x, absX);
        const relativeY = getRelativeY(this.movement.y, absY);
        renderService.renderCharacter(this.texture, this.characterName, relativeX, relativeY, this.movement.angle);
    }

    destroyObj() {
        renderService.removeShip(this.characterName, this.texture);
    }

    blowUp() {
        renderService.blowUpShip(this.texture, this.movement.x, this.movement.y, this.characterName);
        this.destroyObj();
    }

    getDiffX() {
        return this.movement.x - this.movement.prevX;
    }

    getDiffY() {
        return this.movement.y - this.movement.prevY;
    }

    getRenderCoords() {
        return renderService.getRenderCoords(this.texture);
    }
}