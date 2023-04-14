import * as renderService from "../render-service.js";
import * as PIXI from "../libs/pixi.min.js";
import {EquipmentType} from "../const/EquipmentType.js";

export class Equipment extends PIXI.Sprite {
    isEquipped;
    equipmentType;
    id;

    constructor(equipmentType, id, isEquipped) {
        let url;
        switch (equipmentType) {
            case EquipmentType.Engine:
                url = "./images/engine.png";
                break;
            case EquipmentType.FuelTank:
                url = "./images/fuel_tank.png";
                break;
        }
        super(PIXI.Texture.from(url));
        this.id = id;
        this.isEquipped = isEquipped;
        this.equipmentType = equipmentType;
        renderService.initEquipment(this);
    }
}