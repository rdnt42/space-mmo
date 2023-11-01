import * as commandService from "./command-service.js"
import * as initService from "./init-service.js";

let webSocket;
export function initSocketConnection(characterName) {
    webSocket = new WebSocket("ws://localhost:8082/motion/" + characterName);
    webSocket.onclose = onClose();
    webSocket.onopen = onOpen();
    webSocket.onmessage = onMessage();
}

function onClose() {
    return function () {
        console.log("WebSocket connection closed");
    };
}
function onOpen() {
    return async function (event) {
        console.log("WebSocket connection opened", event);
        await new Promise(resolve => setTimeout(resolve, 1000));
        console.log("Try init character", event);
        initService.startInitAll();
    };
}

function onMessage() {
    return function (event) {
        commandService.executeCommand(event.data);
    };
}

export function sendMessage(message) {
    if (message !== "") {
        webSocket.send(JSON.stringify(message));
    }
}