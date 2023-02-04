import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";

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
    return function (p1) {
        console.log("WebSocket connection opened");
    };
}

function onMessage() {
    return function (event) {
        console.log("get message", event);
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