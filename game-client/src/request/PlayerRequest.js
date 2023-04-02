import {commands} from "../const/MessageCommand.js";

export class PlayerMotionRequest {
    constructor(isUpdate, speed, angle) {
        this.command = commands.CMD_UPDATE_CURRENT_PLAYER;
        this.data = {isUpdate, speed, angle}
    }
}