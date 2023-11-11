import {Commands} from "../const/MessageCommand.js";

export class CharacterMotionRequest {
    constructor(isUpdate, forceTypeId, angle) {
        this.command = Commands.UpdateMotion;
        this.data = {isUpdate, forceTypeId, angle}
    }
}

export class CharacterShootingRequest {
    constructor(isShooting, angle) {
        this.command = Commands.UpdateShooting;
        this.data = {isShooting, angle}
    }
}

export class CharacterItemRequest {
    constructor(id, slotId, storageId) {
        this.command = Commands.UpdateInventoryItem;
        this.data = {id, slotId, storageId}
    }
}