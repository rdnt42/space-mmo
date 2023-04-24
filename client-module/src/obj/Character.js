import {FREQUENCY} from "../const/Common.js";
import {renderEngine} from "../render/render-engine.js";


export class Character {
    characterName;
    movement;

    constructor(characterName) {
        this.characterName = characterName;
    };

    initCharacter(x, y, angle, speed, shipTypeId) {
        this.movement = new Movement(Math.round(x / FREQUENCY), Math.round(y / FREQUENCY), angle, speed);
        renderEngine.createCharacterLabel(this.characterName);
        renderEngine.createCharacter(this.characterName, shipTypeId);
    }

    updateCharacter(x, y, angle, speed) {
        this.movement.prevX = this.movement.x;
        this.movement.prevY = this.movement.y;
        this.movement.x = x / FREQUENCY;
        this.movement.y = y / FREQUENCY;
        this.movement.angle = angle;
        this.movement.speed = speed;
    }

    getDiffX() {
        return this.movement.x - this.movement.prevX;
    }

    getDiffY() {
        return this.movement.y - this.movement.prevY;
    }

    getLocation() {
        return "X: " + Math.round(this.movement.x) + " Y: " + Math.round(this.movement.y) + " speed: " +
            this.movement.speed + " angle: " + this.movement.angle;
    }

    destroy() {
        renderEngine.removeCharacter(this);
    }

}

class Movement {
    x;
    y;
    prevX;
    prevY;
    speed;
    angle;
    maxSpeed = 600;

    constructor(x, y, angle, speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
    };

}

