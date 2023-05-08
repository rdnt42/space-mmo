import * as renderEngine from "../render/render.js";
import {EquipmentSlotId} from "../const/EquipmentSlotId.js";
import * as socket from "../websocket-service.js";
import {CharacterItemRequest} from "../request/CharacterRequest.js";

export class EquipmentSlot {
    texture;
    #equipment;

    constructor(equipmentType) {
        this.texture = renderEngine.initEquipmentSlot(equipmentType);
    }

    addToEquipmentSlot(equipment) {
        this.#equipment = equipment;
        this.#equipment.slotId = null;
        renderEngine.addToEquipmentSlot(equipment.texture, this.texture);
        if (this.#equipment.typeId === EquipmentSlotId.Engine) {
            renderEngine.setSpeedLabel(this.#equipment.maxSpeed);
        }
        socket.sendMessage(new CharacterItemRequest(equipment.id, equipment.slotId));
    }

    removeFromEquipmentSlot() {
        if (this.#equipment === undefined) return undefined;

        let removedEquipment = this.#equipment;
        this.#equipment = undefined;
        renderEngine.removeFromEquipmentSlot(removedEquipment.texture);
        // TODO maybe move to inventory
        if (removedEquipment.typeId === EquipmentSlotId.Engine) {
            renderEngine.setSpeedLabel(0);
        }

        return removedEquipment;
    }

    getEquipment() {
        return this.#equipment;
    }
}