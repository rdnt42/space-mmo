import * as PIXI from "../libs/pixi.min.js";
import {initHoldCell} from "../render-service.js";

export class CargoCell extends PIXI.Sprite {
    cargo;
    idx;

    constructor(cargo, idx) {
        super(PIXI.Texture.WHITE);
        this.cargo = cargo;
        this.idx = idx;
        initHoldCell(this, idx);
    }
}