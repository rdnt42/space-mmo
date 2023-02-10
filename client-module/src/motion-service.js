import * as socket from "./websocket-service.js";
import player from "./obj/Player.js"
import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";

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

    //Down
    down.press = () => {
        if (player.speed > -10) {
            player.speed -= 1;
        }
    };

    //Up
    up.press = () => {
        if (player.speed < 10) {
            player.speed += 1;
        }
    };

    //Right
    right.press = () => {
        player.turnRight();
    };

    //Left arrow key `press` method
    left.press = () => {
        player.turnLeft();
    };

    space.press = () => {
        player.speed = 0;
    }

    setInterval(onTimerTick, 16); // 65,5 FPS
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

function onTimerTick() {
    if (player.speed !== 0) {
        let xSpeed = calculateCos(player.angle) * player.speed;
        let ySpeed = calculateSin(player.angle) * player.speed;

        sendMotion(player.x + xSpeed, player.y + ySpeed, true);
    }
}

function calculateCos(deg) {
    return Math.cos((Math.PI / 180) * deg);
}

function calculateSin(deg) {
    return Math.sin((Math.PI / 180) * deg);
}