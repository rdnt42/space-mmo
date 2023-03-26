import * as render from "./render-service.js";
import * as motion from "./motion-service.js"
import {initCharacter} from "./init-service.js";
import {commands} from "./const/MessageCommand.js";

export function executeCommand(response) {
    let socketResponse;
    try {
        socketResponse = JSON.parse(response);
    } catch (e) {
        console.error("caught error when try to parse response", e, response);
        return;
    }

    let command = socketResponse.command;
    switch (command) {
        case commands.CMD_INIT_CURRENT_PLAYER:
            initCharacter(socketResponse.data);

            break;
        case commands.CMD_UPDATE_CURRENT_PLAYER:
            motion.update(socketResponse.data);
            render.updateAllPlayers(socketResponse.data);

            break;
        case commands.CMD_LEAVING_PLAYER:
            render.deletePlayer(socketResponse);

            break;
        default:
            console.error("Unexpected command: " + command);
    }

}