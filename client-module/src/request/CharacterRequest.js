import {Commands} from "../const/MessageCommand.js";

export class CharacterMotionRequest {
    constructor(isUpdate, speed, angle, lastUpdateTime) {
        this.command = Commands.UpdateMotion;
        this.data = {isUpdate, speed, angle, lastUpdateTime}
    }
}

export class CharacterItemRequest {
    constructor(id, slotId) {
        this.command = Commands.UpdateInventoryItem;
        this.data = {id, slotId}
    }
}