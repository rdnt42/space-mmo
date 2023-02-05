import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";
import * as render from "./render-service.js";

let webSocket;
export function initSocketConnection(playerName) {
    webSocket = new WebSocket("ws://localhost:8082/motion/topic/" + playerName);
    webSocket.onclose = onClose();
    webSocket.onopen = onOpen();
    webSocket.onmessage = onMessage();
}

function onClose() {
    return function (p1) {
        console.log("WebSocket connection closed");
    };
}
function onOpen() {
    return function (event) {
        console.log("WebSocket connection opened", event);
        sendMotion(0, 0);
    };
}

function onMessage() {
    return function (event) {
        console.log("get message", event);
        let response = JSON.parse(event.data);
        render.update(response.playerMotions);
    };
}

export function sendMotion(x, y) {
    const motion = new MotionRequest(x, y);
    const request = new PlayerMotionRequest(true, motion);

    sendMessage(request);
}

function sendMessage(message) {
    if (message !== "") {
        webSocket.send(JSON.stringify(message));
        console.log("Send message: ", message);
    }
}