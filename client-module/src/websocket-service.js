import * as commandService from "./command-service.js"
import * as initService from "./init-service.js";

let webSocket;
export function initSocketConnection(playerName) {
    webSocket = new WebSocket("ws://localhost:8082/motion/" + playerName);
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
    return function (event) {
        console.log("WebSocket connection opened", event);
        initService.startInitMotions();
        initService.startInitInventory();
        initService.checkInit();
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