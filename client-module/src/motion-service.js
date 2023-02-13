import * as socket from "./websocket-service.js";
import player from "./obj/Player.js"
import {PlayerMotionRequest} from "./request/PlayerRequest.js";

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


function sendMotion(speed, angle, isUpdate) {
    const request = new PlayerMotionRequest(isUpdate, speed, angle);
    socket.sendMessage(request);
}

export function update(playerResponse) {
    if (playerResponse.playerMotion.playerName !== player.playerName) {
        return;
    }
    player.prevX = player.x;
    player.prevY = player.y;

    let motion = playerResponse.playerMotion.motion;
    player.x = motion.x;
    player.y = motion.y;
    player.angle = motion.angle;
    player.speed = motion.speed;

    return player;
}

export function sendCurrentMotion(isUpdate) {
    sendMotion(player.speed, player.angle, isUpdate);
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

    if (keys.length !== 0) {
        sendCurrentMotion(true);
    } else {
        sendCurrentMotion(false);
    }
}