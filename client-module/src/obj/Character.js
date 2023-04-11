import * as renderService from "../render-service.js";
import {FREQUENCY} from "../const/Common.js";

export class Character {
    characterName;
    movement;
    sprite;
    label;

    constructor(characterName) {
        this.characterName = characterName;
    };

    initCharacter(x, y, angle, speed) {
        this.movement = new Movement(x, y, angle, speed);
        this.sprite = renderService.createCharacterSprite();
        this.label = renderService.createCharacterLabel(this);
    }

    updateCharacter(x, y, angle, speed) {
        this.movement.prevX = this.movement.x;
        this.movement.prevY = this.movement.y;
        this.movement.x = Math.round(x / FREQUENCY);
        this.movement.y = Math.round(y / FREQUENCY);
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
        return "X: " + this.movement.x + " Y: " + this.movement.y + " speed: " +
            this.movement.speed + " angle: " + this.movement.angle;
    }

    render() {
        renderService.renderCharacter(this);
        renderService.renderCharacterLabel(this);
    }

    destroy() {
        renderService.removeCharacter(this);
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

