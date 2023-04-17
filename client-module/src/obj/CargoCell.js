import * as PIXI from "../libs/pixi.min.js";
import * as renderService from "../render-service.js";

export class CargoCell extends PIXI.Sprite {
    #cargo;
    idx;

    constructor(cargo, idx) {
        super(PIXI.Texture.WHITE);
        this.#cargo = cargo;
        this.idx = idx;
        renderService.initCargoCell(this, idx);
    }

    getCargo() {
        return this.#cargo;
    }

    addToCargoCell(cargo) {
        renderService.addToCargoCell(cargo, this);
        this.#cargo = cargo;
        cargo.visible = true;
    }

    removeFromCargoCell() {
        if (this.#cargo === undefined) return undefined;

        let removedCargo = this.#cargo;
        this.#cargo = undefined;
        removedCargo.visible = false;

        return removedCargo;
    }
}