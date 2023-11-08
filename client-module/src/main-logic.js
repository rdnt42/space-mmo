import * as keyboard from "./keyboard-service.js"
import {Direction} from "./const/Direction.js";
import {FREQUENCY_TIME} from "./const/Common.js";
import * as inventoryService from "./inventory-service.js";
import * as weaponService from "./weapon-service.js";
import {InteractiveState} from "./const/InteractiveState.js";
import * as bulletService from "./obj-service/bullet-setvice.js";
import * as shipService from "./obj-service/ship-service.js";
import {CharacterMotionRequest, CharacterShootingRequest} from "./message/CharacterMessage.js";
import * as socket from "./websocket-service.js";

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
                weaponService.stopWeapon();
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
    let ship = shipService.getPlayerShip();

    let engine = inventoryService.getEngine();
    if (engine !== null) {
        let move = getCharacterMotion(ship.movement.speed, engine.maxSpeed, ship.movement.angle);
        // TODO service for sending
        const request = new CharacterMotionRequest(true, move.forceTypeId, move.angle);
        socket.sendMessage(request);
    }
}

function sendShootingInfo() {
    // in client coordinates render from top left point, we need to invert it by 180 degrees
    let angle = getCharacterAngle(mouseCoords.x, mouseCoords.y) + 180;
    const request = new CharacterShootingRequest(weaponService.getShotState(), angle);
    socket.sendMessage(request);
}

function clearUnusedObjects() {
    removeOrMarkObjects(bulletService.getObjectsMap());
}

function removeOrMarkObjects(map) {
    map.forEach((obj, key, map) => {
        if (obj.isUpdated === false) {
            obj.destroyObj();
            map.delete(key);
        }
        obj.isUpdated = false;
    });
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
    if (state !== InteractiveState.Space) return

    weaponService.useWeapon();
}

function onMouseUp() {
    if (state !== InteractiveState.Space) return
    weaponService.stopWeapon();
}

function getCharacterAngle(x, y) {
    let ship = shipService.getPlayerShip();
    let coords = ship.getRenderCoords();

    return Math.atan2(coords.y - y, coords.x - x) * (180 / Math.PI);
}