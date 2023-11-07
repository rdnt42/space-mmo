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

