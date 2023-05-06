import * as renderEngine from "../render/engine.js";

export class CargoCell {
    texture;

    #cargo;
    idx;

    constructor(cargo, idx) {
        this.#cargo = cargo;
        this.idx = idx;
        this.texture = renderEngine.initCargoCell(idx);
    }

    getCargo() {
        return this.#cargo;
    }

    addToCargoCell(cargo) {
        renderEngine.addToCargoCell(cargo.texture, this.texture);
        this.#cargo = cargo;
        cargo.slotId = this.idx;
    }

    removeFromCargoCell() {
        if (this.#cargo === undefined) return undefined;

        let removedCargo = this.#cargo;
        this.#cargo = undefined;
        renderEngine.removeFromCargoCell(removedCargo.texture);
        removedCargo.slotId = null;

        return removedCargo;
    }
}