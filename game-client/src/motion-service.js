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

export function update(data) {
    let motion = data.playerMotion;
    if (motion.playerName !== player.playerName) {
        return;
    }
    player.prevX = player.x;

    player.prevY = player.y;
    player.x = motion.x;
    player.y = motion.y;
    player.angle = motion.angle;
    player.speed = motion.speed;

    return player;
}

export function sendCurrentMotion(updatePlayer, isUpdate) {
    sendMotion(updatePlayer.speed, updatePlayer.angle, isUpdate);
}

function onTimerTick() {
    const copyPlayer = structuredClone(player);
    // Left
    if (keys["a"]) {
        if((copyPlayer.angle -= 5) < 0) {
            copyPlayer.angle = copyPlayer.angle % 360 + 360;
        }
    }

    // Right
    if (keys["d"]) {
        if((copyPlayer.angle += 5) >= 360) {
            copyPlayer.angle = copyPlayer.angle % 360;
        }
    }

    // Up
    if (keys["w"]) {
        if (copyPlayer.speed < copyPlayer.maxSpeed) {
            copyPlayer.speed += 1;
        }
    }

    // Down
    if (keys["s"]) {
        if (copyPlayer.speed > (copyPlayer.maxSpeed / 2) * -1) {
            copyPlayer.speed -= 1;
        }
    }

    if (keys[" "]) {
        copyPlayer.speed = 0;
    }

    if (keys.length !== 0) {
        sendCurrentMotion(copyPlayer, true);
    } else {
        sendCurrentMotion(copyPlayer, false);
    }
}