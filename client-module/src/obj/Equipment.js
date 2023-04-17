import * as renderService from "../render-service.js";
import * as PIXI from "../libs/pixi.min.js";
import {EquipmentType} from "../const/EquipmentType.js";

export class Equipment extends PIXI.Sprite {
    isEquipped;
    equipmentType;

    constructor(equipmentType, idx, isEquipped) {
        let url;
        switch (equipmentType) {
            case EquipmentType.Engine:
                url = "./images/engine" + idx + ".gif";
                break;
            case EquipmentType.FuelTank:
                url = "./images/fuel_tank" + idx + ".gif";
                break;
        }
        super(PIXI.Texture.from(url));
        this.isEquipped = isEquipped;
        this.equipmentType = equipmentType;
        renderService.initEquipment(this);
    }
}