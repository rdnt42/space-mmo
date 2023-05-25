import * as keyboard from "./keyboard-service.js"
import {Direction} from "./const/Direction.js";
import * as characterService from "./character-service.js";
import {FREQUENCY_TIME} from "./const/Common.js";
import * as inventoryService from "./inventory-service.js";

const accelerationTime = 1500;

export function mainLogicInit() {
    setInterval(worldTick, FREQUENCY_TIME);
}

function worldTick() {
    let character = characterService.getPlayerCharacter();

    let engine = inventoryService.getEngine();
    if (engine !== null) {
        let move = getPlayerDirectionAndSpeed(character.movement.speed, engine.maxSpeed, character.movement.angle);
        characterService.sendMotion(move.forceTypeId, move.angle, true);
    }
}

function getPlayerDirectionAndSpeed(speed, maxSpeed, angle) {
    let directions = keyboard.getDirections();
    let forceTypeId = 0;
    for (const direction of directions) {
        if (direction === Direction.Stop) {
            forceTypeId = 2;
        } else if (direction === Direction.Up && speed < maxSpeed) {
            forceTypeId = 1;
        } else if (direction === Direction.Down && speed > (maxSpeed / 2) * -1) {
            forceTypeId = -1;
        }

        if (direction === Direction.Left && (angle -= 5) < 0) {
            angle = angle % 360 + 360;
        } else if (direction === Direction.Right && (angle += 5) >= 360) {
            angle = angle % 360;
        }
    }

    return {
        forceTypeId: forceTypeId,
        angle: angle,
    }
}