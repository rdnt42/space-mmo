import * as renderService from "../render-service.js";

export class CargoCell {
    texture;

    #cargo;
    idx;

    constructor(cargo, idx) {
        this.#cargo = cargo;
        this.idx = idx;
        this.texture = renderService.initCargoCell(idx);
    }

    getCargo() {
        return this.#cargo;
    }

    addToCargoCell(cargo) {
        renderService.addToCargoCell(cargo.texture, this.texture);
        this.#cargo = cargo;
    }

    removeFromCargoCell() {
        if (this.#cargo === undefined) return undefined;

        let removedCargo = this.#cargo;
        this.#cargo = undefined;
        renderService.removeFromCargoCell(removedCargo.texture)

        return removedCargo;
    }
}