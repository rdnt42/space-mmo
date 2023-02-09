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
            if (key.isUp && key.press) {
                key.press();
            }
            key.isDown = true;
            key.isUp = false;
            event.preventDefault();
        }
    };

    //The `upHandler`
    key.upHandler = (event) => {
        if (event.key === key.value) {
            if (key.isDown && key.release) {
                key.release();
            }
            key.isDown = false;
            key.isUp = true;
            event.preventDefault();
        }
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
        down = keyboard("s");

    //Left arrow key `press` method
    left.press = () => {
        //Change the cat's velocity when the key is pressed
        // spaceship.rotation = 4.712;
        sendMotion(player.x - 15, player.y, true);
    };

    //Left arrow key `release` method
    left.release = () => {
        //If the left arrow has been released, and the right arrow isn't down,
        //and the cat isn't moving vertically:
        //Stop the cat
        // if (!right.isDown && spaceship.vy === 0) {
        //     spaceship.vx = 0;
        // }
    };

    //Up
    up.press = () => {
        // spaceship.rotation = 0;
        sendMotion(player.x, player.y - 15, true);
    };
    up.release = () => {
        // if (!down.isDown && spaceship.vx === 0) {
        //     spaceship.vy = 0;
        // }
    };

    //Right
    right.press = () => {
        // spaceship.rotation = 1.571;
        sendMotion(player.x + 15, player.y, true);
    };
    right.release = () => {
        // if (!left.isDown && spaceship.vy === 0) {
        //     spaceship.vx = 0;
        // }
    };

    //Down
    down.press = () => {
        // spaceship.rotation = 3.142;
        sendMotion(player.x, player.y + 15, true);

    };
    down.release = () => {
        // if (!up.isDown && spaceship.vx === 0) {
        //     spaceship.vy = 0;
        // }
    };
}


function sendMotion(x, y, isUpdate) {
    const motion = new MotionRequest(x, y);
    const request = new PlayerMotionRequest(isUpdate, motion);

    socket.sendMessage(request);
}

export function update(playerResponse) {
    if(playerResponse.playerMotion.playerName !== player.playerName) {
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