import * as renderService from "../render-service.js";

export class Equipment {
    equipmentType;

    initEquipment(equipmentType) {
        renderService.createEquipment(equipmentType);
    }
}