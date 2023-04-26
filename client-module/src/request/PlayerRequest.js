import {Commands} from "../const/MessageCommand.js";

export class PlayerMotionRequest {
    constructor(isUpdate, speed, angle, lastUpdateTime) {
        this.command = Commands.UpdatePlayer;
        this.data = {isUpdate, speed, angle, lastUpdateTime}
    }
}