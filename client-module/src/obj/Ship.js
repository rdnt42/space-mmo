import * as renderService from "../render/render.js";

export class Ship {
    texture;
    characterName;
    movement;
    isUpdated;

    constructor(characterName, x, y, angle, speed, typeId, polygon) {
        this.texture = renderService.createCharacter(characterName, typeId, x, y, angle, polygon);
        this.characterName = characterName;
        this.updateObj(x, y, angle, speed);
    }

    updateObj(x, y, angle, speed) {
        this.x = x;
        this.y = y;
        this.movement = {
            prevX: this.x,
            prevY: this.y,
            x: x,
            y: y,
            angle: angle,
            speed: speed
        }
        this.isUpdated = true;
    }

    renderObj() {
        renderService.renderCharacter(this.texture, this.characterName, this.movement.x, this.movement.y, this.movement.angle);
    }

    destroyObj() {
        renderService.removeShip(this.characterName, this.texture);
    }

    blowUp() {
        renderService.blowUpShip(this.characterName);
    }

    getDiffX() {
        return this.movement.x - this.movement.prevX;
    }

    getDiffY() {
        return this.movement.y - this.movement.prevY;
    }

    getRenderCoords() {
        return render.getRenderCoords(this.texture);
    }
}