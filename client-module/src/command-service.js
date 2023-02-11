import * as render from "./render-service.js";
import * as motion from "./motion-service.js"

export function executeCommand(response) {
    let parseObj;
    try {
        parseObj = JSON.parse(response);
    } catch (e) {
        console.error("caught error when try to parse response", e, response);
        return;
    }

    let command = parseObj.command;
    switch (command) {
        case "CMD_UPDATE_CURRENT_PLAYER":
            motion.update(parseObj);
            render.updateCurrentPlayer(parseObj);

            // TODO move to another service
            motion.sendCurrentMotion();
            break;
        case "CMD_UPDATE_OTHER_PLAYER":
            motion.update(parseObj);
            render.updateSingle(parseObj);
            break;
        case "CMD_LEAVING_PLAYER":
            render.deletePlayer(parseObj);
            break;
        default:
            console.error("Unexpected command: " + command);
    }

}