import {commands} from "../const/MessageCommand.js";

export class PlayerInitRequest {
    constructor() {
        this.command = commands.CMD_INIT_CURRENT_PLAYER;
    }
}