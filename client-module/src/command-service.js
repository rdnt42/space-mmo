import * as render from "./render-service.js";
import * as motion from "./motion-service.js"
import {sendCurrentMotion} from "./motion-service.js";

export function executeCommand(response) {
    let obj;
    try {
        obj = JSON.parse(response);
    } catch (e) {
        console.error("caught error when try to parse response", e, response);
        return;
    }

    let command = obj.command;
    switch (command) {
        case "CMD_UPDATE_CURRENT_PLAYER":
            console.log("CMD_UPDATE_CURRENT_PLAYER");
            motion.update(obj);
            render.updateCurrentPlayer(obj);

            // TODO move to another service
            motion.sendCurrentMotion();
            break;
        case "CMD_UPDATE_OTHER_PLAYER":
            console.log("CMD_UPDATE_OTHER_PLAYER");
            motion.update(obj);
            render.updateSingle(obj);
            break;
        case "CMD_LEAVING_PLAYER":
            console.log("CMD_LEAVING_PLAYER");
            break;
        default:
            console.error("Unexpected command: " + command);
    }

}