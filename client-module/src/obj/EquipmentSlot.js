import * as PIXI from "../libs/pixi.min.js";
import {initEquipmentSlot} from "../render-service.js";

export class EquipmentSlot extends PIXI.Sprite {
    equipment;

    constructor(equipmentType) {
        super(PIXI.Texture.WHITE);
        initEquipmentSlot(this, equipmentType);
    }
}