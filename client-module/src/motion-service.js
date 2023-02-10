import * as socket from "./websocket-service.js";
import player from "./obj/Player.js"
import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";

// Keep track of which keys are currently pressed
let keys = {};

document.addEventListener("keydown", function(event) {
    keys[event.key] = true;
});

document.addEventListener("keyup", function(event) {
    keys[event.key] = false;
});

export function keyBoardInit() {
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

    // Left
    if (keys["a"]) {
        player.turnLeft();
    }

    // Right
    if (keys["d"]) {
        player.turnRight();
    }

    // Up
    if (keys["w"]) {
        player.increaseSpeed();
    }

    // Down
    if (keys["s"]) {
        player.decreaseSpeed();
    }

    if (keys[" "]) {
        player.speed = 0;
    }

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