import * as keyboard from "./keyboard-service.js"
import {Direction} from "./const/Direction.js";
import * as characterService from "./character-service.js";
import {FREQUENCY_TIME} from "./const/Common.js";

const accelerationTime = 1500;

export function mainLogicInit() {
    setInterval(worldTick, FREQUENCY_TIME);
}

function worldTick() {
    let character = characterService.getPlayerCharacter();
    let move = getPlayerDirectionAndSpeed(character.movement.speed, character.movement.maxSpeed, character.movement.angle);
    characterService.sendMotion(move.speed, move.angle, true);
}

function getPlayerDirectionAndSpeed(speed, maxSpeed, angle) {
    let currSpeedDiff = (maxSpeed * FREQUENCY_TIME) / accelerationTime;

    let directions = keyboard.getDirections();
    for (const direction of directions) {
        if (direction === Direction.Stop) {
            speed -= currSpeedDiff;
            if(speed < 0) speed = 0;
        } else if (direction === Direction.Up) {
            speed += currSpeedDiff;
            if (speed > maxSpeed) speed = maxSpeed;
        } else if (direction === Direction.Down) {
            speed -= currSpeedDiff;
            let minSpeed = (maxSpeed / 2) * -1;
            if (speed < minSpeed) speed = minSpeed;
        }

        if (direction === Direction.Left && (angle -= 5) < 0) {
            angle = angle % 360 + 360;
        } else if (direction === Direction.Right && (angle += 5) >= 360) {
            angle = angle % 360;
        }
    }

    return {
        speed: speed,
        angle: angle,
    }
}