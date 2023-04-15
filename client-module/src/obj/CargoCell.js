import * as PIXI from "../libs/pixi.min.js";
import {initCargoCell} from "../render-service.js";

export class CargoCell extends PIXI.Sprite {
    cargo;
    idx;

    constructor(cargo, idx) {
        super(PIXI.Texture.WHITE);
        this.cargo = cargo;
        this.idx = idx;
        initCargoCell(this, idx);
    }
}