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

