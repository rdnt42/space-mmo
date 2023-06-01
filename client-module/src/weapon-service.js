// TODO
let shotState = false;
let shotAngle;

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

// TODO to init method
window.addEventListener("mousedown", onMouseDown);
window.addEventListener("mouseup", onMouseUp);

function onMouseDown() {
    // TODO rework
    useWeapon();
}

function onMouseUp() {
    stopWeapon();
}