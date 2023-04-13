import {Commands} from "../const/MessageCommand.js";

export class PlayerInitRequest {
    constructor() {
        this.command = Commands.InitPlayer;
    }
}