import * as characterService from "../character-service.js";
import {getRelativeX, getRelativeY} from "./obj-utils.js";
import {Bullet} from "../obj/Bullet.js";

let bullets = new Map();

export function createOrUpdate(objs) {
    for (const obj of objs) {
        createOrUpdateObj(obj.id, obj.x, obj.y, obj.angle, obj.type)
    }
}

function createOrUpdateObj(id, x, y, angle, type) {
    let abs = characterService.getPlayerCharacter().movement;
    let newX = getRelativeX(x, abs.x);
    let newY = getRelativeY(y, abs.y);
    let bullet = bullets.get(id);
    if (bullet === undefined) {
        bullet = new Bullet(newX, newY, angle, type);
        bullets.set(id, bullet);
    } else {
        bullet.updateObj(newX, newY, angle);
    }
    bullet.renderObj();
}

export function getObjectsMap() {
    return bullets;
}