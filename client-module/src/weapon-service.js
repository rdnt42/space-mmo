import * as renderEngine from "./render/engine.js";

let shotState = false;
let isUpdated = false;

export function getShotState() {
    return shotState;
}

export function useWeapon() {
    shotState = true;
    isUpdated = false;
}

export function stopWeapon() {
    shotState = false;
    isUpdated = false;
}

export function updateBulletData(bullets) {
    for (const bullet of bullets) {
        renderEngine.createOrUpdateBullet(bullet.id, bullet.x, bullet.y, bullet.angle);
    }
}

