import * as renderEngine from "./render/engine.js";

let shotState = false;
let isUpdated = false;

export function isNeedShotUpdate() {
    return !isUpdated;
}

export function getShotState() {
    return shotState;
}

export function setUpdated() {
    isUpdated = true;
}

export function useWeapon() {
    shotState = true;
    isUpdated = false;
}

export function stopWeapon() {
    shotState = false;
    isUpdated = false;
}

export function updateBulletData(data) {
    renderEngine.createOrUpdateBullet(data.id, data.x, data.y, data.angle);
}

