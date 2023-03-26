import * as socket from "./websocket-service.js";
import {PlayerInitRequest} from "./request/PlayerInitRequest.js";

export function sendInitCharacter() {
    socket.sendMessage(new PlayerInitRequest());
}