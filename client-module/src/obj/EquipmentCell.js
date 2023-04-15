import * as PIXI from "../libs/pixi.min.js";
import {initEquipmentCell} from "../render-service";

export class EquipmentCell extends PIXI.Sprite {
    equipment;

    constructor(equipment) {
        super(PIXI.Texture.WHITE);
        this.equipment = equipment;
        initEquipmentCell(this, equipment.equipmentType);
    }
}