import {Commands} from "../const/MessageCommand.js";

export class PlayerMotionRequest {
    constructor(isUpdate, speed, angle) {
        this.command = Commands.UpdatePlayer;
        this.data = {isUpdate, speed, angle}
    }
}