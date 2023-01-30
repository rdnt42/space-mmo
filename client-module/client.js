import { WebSocket } from "ws"

const webSocket = new WebSocket("ws://localhost:8082/motion/topic/pers2");

webSocket.onclose = function () { alert("WebSocket connection closed") };

//Send a message if it's not empty, then clear the input field
function sendMessage(message) {
    if (message !== "") {
        webSocket.send(message);
        // id("message").value = "";
    }
}