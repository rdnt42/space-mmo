import * as render from "../render/render.js";

export class CargoCell {
    texture;

    #cargo;
    idx;

    constructor(cargo, idx) {
        this.#cargo = cargo;
        this.idx = idx;
        this.texture = render.initCargoCell(idx);
    }

    getCargo() {
        return this.#cargo;
    }

    addToCargoCell(cargo) {
        render.addToCargoCell(cargo.texture, this.texture);

        this.#cargo = cargo;
        cargo.slotId = this.idx;
    }

    removeFromCargoCell() {
        if (this.#cargo === undefined) return undefined;

        let removedCargo = this.#cargo;
        this.#cargo = undefined;
        render.removeFromCargoCell(removedCargo.texture);
        removedCargo.slotId = null;

        return removedCargo;
    }
}