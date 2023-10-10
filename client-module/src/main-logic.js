import * as keyboard from "./keyboard-service.js"
import {Direction} from "./const/Direction.js";
import * as characterService from "./character-service.js";
import {FREQUENCY_TIME} from "./const/Common.js";
import * as inventoryService from "./inventory-service.js";
import * as weaponService from "./weapon-service.js";
import {InteractiveState} from "./const/InteractiveState.js";
import * as renderEngine from "./render/engine.js";

let state;
let mouseCoords = {};

export function mainLogicInit() {
    setInterval(worldTick, FREQUENCY_TIME);
    setInterval(clearUnusedObjects, FREQUENCY_TIME * 2);
    window.addEventListener("mousemove", onMouseMove);
    window.addEventListener("mousedown", onMouseDown);
    window.addEventListener("mouseup", onMouseUp);
    state = InteractiveState.Space;

    document.addEventListener("keydown", (event) => {
        event.preventDefault();
        if (event.key === "i") {
            if (inventoryService.changeInventoryState()) {
                state = InteractiveState.InventoryOpen;
            } else {
                state = InteractiveState.Space;
            }
        }
    });
}

function worldTick() {
    sendMotionInfo();
    sendShootingInfo();
}

function sendMotionInfo() {
    let character = characterService.getPlayerCharacter();

    let engine = inventoryService.getEngine();
    if (engine !== null) {
        let move = getCharacterMotion(character.movement.speed, engine.maxSpeed, character.movement.angle);
        characterService.sendMotion(move.forceTypeId, move.angle, true);
    }
}

function sendShootingInfo() {
    // in client coordinates render from top left point, we need to invert it by 180 degrees
    let angle = getCharacterAngle(mouseCoords.x, mouseCoords.y) + 180;
    characterService.sendShooting(weaponService.getShotState(), angle);
}

function clearUnusedObjects() {
    renderEngine.clearUnusedObjects()
}

function getCharacterMotion(speed, maxSpeed, angle) {
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

function onMouseMove(event) {
    mouseCoords.x = event.offsetX;
    mouseCoords.y = event.offsetY;
}

function onMouseDown() {
    if (!state === InteractiveState.Space) return
    weaponService.useWeapon();
}

function onMouseUp() {
    if (!state === InteractiveState.Space) return
    weaponService.stopWeapon();
}

function getCharacterAngle(x, y) {
    let character = characterService.getPlayerCharacter();
    let coords = renderEngine.getRenderCoords(character.characterName);

    return Math.atan2(coords.y - y, coords.x - x) * (180 / Math.PI);
}