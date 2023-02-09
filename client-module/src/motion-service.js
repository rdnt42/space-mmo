import * as socket from "./websocket-service.js";
import player from "./obj/Player.js"
import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";

let vx = 0;
let vy = 0;

function keyboard(value) {
    const key = {};
    key.value = value;
    key.isDown = false;
    key.isUp = true;
    key.press = undefined;
    key.release = undefined;
    //The `downHandler`
    key.downHandler = (event) => {
        if (event.key === key.value) {
            key.press();
        }
    };

    //The `upHandler`
    key.upHandler = (event) => {
    };

    //Attach event listeners
    const downListener = key.downHandler.bind(key);
    const upListener = key.upHandler.bind(key);

    window.addEventListener("keydown", downListener, false);
    window.addEventListener("keyup", upListener, false);

    // Detach event listeners
    key.unsubscribe = () => {
        window.removeEventListener("keydown", downListener);
        window.removeEventListener("keyup", upListener);
    };

    return key;
}

export function keyBoardInit() {
    //Capture the keyboard arrow keys
    const left = keyboard("a"),
        up = keyboard("w"),
        right = keyboard("d"),
        down = keyboard("s"),
        space = keyboard(" ");


    //Left arrow key `press` method
    left.press = () => {
        // spaceship.rotation = 4.712;
        if (vx > -20) {
            vx -= 1;
        }
    };

    //Up
    up.press = () => {
        // spaceship.rotation = 0;
        if (vy > -20) {
            vy -= 1;
        }

    };

    //Right
    right.press = () => {
        // spaceship.rotation = 1.571;
        if (vx < 20) {
            vx += 1;
        }
    };

    //Down
    down.press = () => {
        // spaceship.rotation = 3.142;
        if (vy < 20) {
            vy += 1;
        }
    };

    space.press = () => {
        vx = 0;
        vy = 0;
    }
}


function sendMotion(x, y, isUpdate) {
    const motion = new MotionRequest(x, y);
    const request = new PlayerMotionRequest(isUpdate, motion);

    socket.sendMessage(request);
}

export function update(playerResponse) {
    if (playerResponse.playerMotion.playerName !== player.playerName) {
        return;
    }
    player.prevX = player.x;
    player.prevY = player.y;

    player.x = playerResponse.playerMotion.motion.x;
    player.y = playerResponse.playerMotion.motion.y;
}

export function sendCurrentMotion() {
    sendMotion(player.x, player.y, true);
}

setInterval(onTimerTick, 16); // 65,5 FPS

function onTimerTick() {
    if (vx !== 0 || vy !== 0) {
        sendMotion(player.x + vx, player.y + vy, true);
    }
}