import * as commandService from "./command-service.js"
import player from "./obj/Player.js";

let webSocket;
export function initSocketConnection(playerName) {
    webSocket = new WebSocket("ws://localhost:8082/motion/" + playerName);
    webSocket.onclose = onClose();
    webSocket.onopen = onOpen(playerName);
    webSocket.onmessage = onMessage();
}

function onClose() {
    return function (p1) {
        console.log("WebSocket connection closed");
    };
}
function onOpen(playerName) {
    return function (event) {
        // TODO move to another service
        player.playerName = playerName;
        console.log("WebSocket connection opened", event);
    };
}

function onMessage() {
    return function (event) {
        // console.log("get message", event);
        commandService.executeCommand(event.data);
    };
}

export function sendMessage(message) {
    if (message !== "") {
        webSocket.send(JSON.stringify(message));
        // console.log("Send message: ", message);
    }
}