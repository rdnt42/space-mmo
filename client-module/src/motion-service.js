import player from "./obj/Player.js"


export function update(data) {
    let motion = data.playerMotion;
    if (motion.playerName !== player.playerName) {
        return;
    }
    player.prevX = player.x;

    player.prevY = player.y;
    player.x = Math.round(motion.x / 60);
    player.y = Math.round(motion.y / 60);
    player.angle = motion.angle;
    player.speed = motion.speed;

    return player;
}

export function sendCurrentMotion(updatePlayer, isUpdate) {
    sendMotion(updatePlayer.speed, updatePlayer.angle, isUpdate);
}