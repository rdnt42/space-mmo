import {Direction} from "./const/Direction.js";

let keys = {};

export function initKeyBoard() {
    document.addEventListener("keydown", function (event) {
        event.preventDefault();
        keys[event.key] = true;
    });

    document.addEventListener("keyup", function (event) {
        event.preventDefault();
        keys[event.key] = false;
    });
}


export function getDirections() {
    let dirs = [];
    if (keys["a"]) {
        dirs.push(Direction.Left);
    }

    if (keys["d"]) {
        dirs.push(Direction.Right);
    }

    if (keys["w"]) {
        dirs.push(Direction.Up);
    }

    if (keys["s"]) {
        dirs.push(Direction.Down);
    }

    if (keys[" "]) {
        dirs.push(Direction.Stop);
    }

    return dirs;
}
