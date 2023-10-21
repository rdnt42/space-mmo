import {Commands} from "../const/MessageCommand.js";

export class CharacterMotionRequest {
    constructor(isUpdate, forceTypeId, angle) {
        this.command = Commands.UpdateMotion;
        this.data = {isUpdate, forceTypeId, angle}
    }
}

export class CharacterShootingRequest {
    constructor(isShooting, angle, lastUpdateTime) {
        this.command = Commands.UpdateShooting;
        this.data = {isShooting, angle, lastUpdateTime}
    }
}

export class CharacterItemRequest {
    constructor(id, slotId, storageId) {
        this.command = Commands.UpdateInventoryItem;
        this.data = {id, slotId, storageId}
    }
}

export class CharacterResponse {
    constructor(data) {
        this.characterName = data.id;
        this.x = data.x;
        this.y = data.y;
        this.angle = data.angle;
        this.speed = data.speed;
        this.shipTypeId = data.shipTypeId;
        this.hp = data.hp;
        this.evasion = data.evasion;
        this.armor = data.armor;
    }
}