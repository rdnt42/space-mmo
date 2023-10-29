import * as renderEngine from "../render/engine.js";

export class Character {
    characterName;
    movement;

    constructor(characterName) {
        this.characterName = characterName;
    };

    initCharacter(x, y, angle, speed, shipTypeId, polygon) {
        this.movement = new Movement(x, y, angle, speed);
        renderEngine.createCharacter(this.characterName, shipTypeId, x, y, angle, polygon);
    }

    updateCharacter(x, y, angle, speed) {
        this.movement.prevX = this.movement.x;
        this.movement.prevY = this.movement.y;
        this.movement.x = x;
        this.movement.y = y;
        this.movement.angle = angle;
        this.movement.speed = speed;
        this.render();
    }

    render() {
        renderEngine.renderCharacter(this.characterName, this.movement.x, this.movement.y, this.movement.angle);
    }

    getDiffX() {
        return this.movement.x - this.movement.prevX;
    }

    getDiffY() {
        return this.movement.y - this.movement.prevY;
    }

    getLocation() {
        return "X: " + Math.round(this.movement.x) + " Y: " + Math.round(this.movement.y) + " speed: " +
            Math.round(this.movement.speed) + " angle: " + this.movement.angle;
    }

    destroy() {
        renderEngine.removeCharacter(this.characterName);
    }
}

class Movement {
    x;
    y;
    prevX;
    prevY;
    speed;
    angle;

    constructor(x, y, angle, speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
    };

}

