// TODO
let shotState = false;
let shotAngle;

export function initWeapons() {
}

export function isNeedShotUpdate() {
    return shotState;
}

export function useWeapon() {
    shotState = true;
    console.log("shot: " + shotState)
}

export function stopWeapon() {
    shotState = false;
    console.log("shot: " + shotState)
}

