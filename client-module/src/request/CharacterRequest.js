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

export class CharacterResponse {
    constructor(data) {
        this.characterName = data.characterName;
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