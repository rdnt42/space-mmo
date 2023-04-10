import {commands} from "../const/MessageCommand.js";

export class PlayerMotionRequest {
    constructor(isUpdate, speed, angle) {
        this.command = commands.UpdatePlayer;
        this.data = {isUpdate, speed, angle}
    }
}