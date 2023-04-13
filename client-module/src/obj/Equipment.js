import * as renderService from "../render-service.js";

export class Equipment {
    equipmentType;
    idx;

    initEquipment(equipmentType, idx) {
        this.equipmentType = equipmentType;
        this.idx = idx;
        renderService.createEquipment(equipmentType, idx);
    }
}