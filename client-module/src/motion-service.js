import * as socket from "./websocket-service.js";
import player from "./obj/Player.js"
import {PlayerMotionRequest} from "./request/PlayerRequest.js";

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