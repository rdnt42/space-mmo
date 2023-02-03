import {MotionRequest, PlayerMotionRequest} from "./request/PlayerRequest.js";


createConnectInfo();

function createConnectInfo() {
    let input = document.createElement("input");
    input.innerHTML = "InputText";
    input.type = "text";
    input.name = "inputText";
    document.body.appendChild(input);

    let btn = document.createElement("button");
    btn.innerHTML = "Submit";
    btn.type = "submit";
    btn.name = "formBtn";
    document.body.appendChild(btn);
    btn.onclick = function () {
        console.log("playerName: " + input.value)
        initSocketConnection(input.value);
        input.remove();
        btn.remove();
    };
}

export function sendMotion(x, y) {
    const motion = new MotionRequest(x, y);
    const request = new PlayerMotionRequest(true, motion);

    sendMessage(request);
}

function initSocketConnection(playerName) {
    const webSocket = new WebSocket("ws://localhost:8082/motion/topic/" + playerName);
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
        const msg = JSON.parse(event.data);
    };
}

function sendMessage(message) {
    if (message !== "") {
        webSocket.send(JSON.stringify(message));
        console.log("Send message: ", message);
    }
}