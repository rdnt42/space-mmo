import * as render from "./render-service.js";
import * as motion from "./motion-service.js"

let webSocket;
export function initSocketConnection(playerName) {
    webSocket = new WebSocket("ws://localhost:8082/motion/" + playerName);
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
    };
}

function onMessage() {
    return function (event) {
        console.log("get message", event);
        let response = JSON.parse(event.data);
        render.update(response);
        motion.update(response)
    };
}

export function sendMessage(message) {
    if (message !== "") {
        webSocket.send(JSON.stringify(message));
        console.log("Send message: ", message);
    }
}