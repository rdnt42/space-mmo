import * as render from "./render-service.js";
import * as initService from "./init-service.js";
import * as characterService from "./character-service.js";
import {Commands} from "./const/MessageCommand.js";

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
        case Commands.InitPlayer:
            initService.initCharacter(socketResponse.data);
            characterService.updateOrCreateCharacters(socketResponse.data);

            break;
        case Commands.UpdatePlayer:
            characterService.updateOrCreateCharacters(socketResponse.data);

            break;
        case Commands.LeavingPlayer:
            render.removeCharacter(socketResponse.data);

            break;
        default:
            console.error("Unexpected command: " + command);
    }

}