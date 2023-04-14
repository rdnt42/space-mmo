import * as renderService from "../render-service.js";
import * as PIXI from "../libs/pixi.min.js";
import {EquipmentType} from "../const/EquipmentType.js";

export class Equipment extends PIXI.Sprite {
    equipmentType;
    idx;
    id;

    constructor(equipmentType, idx, id) {
        let url;
        switch (equipmentType) {
            case EquipmentType.Engine:
                url = "./images/engine.png";
        }
        super(PIXI.Texture.from(url));
        this.idx = idx;
        this.id = id;
        this.equipmentType = equipmentType;
        renderService.initEquipment(this, idx);
    }
}