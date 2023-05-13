import * as render from "../render/render.js";

export class CargoCell {
    texture;

    #cargo = null;
    idx;

    constructor(cargo, idx) {
        this.#cargo = cargo;
        this.idx = idx;
        this.texture = render.initCargoCell(idx);
    }

    getItem() {
        return this.#cargo;
    }

    add(cargo) {
        this.#cargo = cargo;

        if (cargo !== null) {
            cargo.slotId = this.idx;
            cargo.slot = this;
            render.addToCargoCell(cargo.texture, this.texture);
        }
    }

    remove() {
        if (this.#cargo === null) return null;

        let removedCargo = this.#cargo;
        this.#cargo = null;
        removedCargo.slotId = null;
        removedCargo.slot = null;
        render.removeFromCargoCell(removedCargo.texture);

        return removedCargo;
    }

    swapCargo(newCell) {
        let newItem = newCell.getItem();
        newCell.add(this.#cargo);
        this.add(newItem);
    }

    center() {
        render.addToCargoCell(this.#cargo.texture, this.texture);
    }
}