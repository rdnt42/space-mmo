import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";

const webSocket = new WebSocket("ws://localhost:8082/motion/topic/pers2");

webSocket.onclose = function () { console.log("WebSocket connection closed") };

webSocket.onopen = function () { console.log("WebSocket connection opened") };

export function sendMotion(x, y) {
    const motion = new MotionRequest(x, y);
    const request = new PlayerMotionRequest(true, motion);

    sendMessage(request);
}

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(JSON.stringify(message));
        console.log("Send message: ", message);
    }
}

