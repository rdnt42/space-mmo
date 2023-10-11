import * as render from "../render/render.js";
import {HOLD_STORAGE_ID} from "../const/Common.js";

export class CargoCell {
    static storageId = HOLD_STORAGE_ID;
    texture;

    #cargo = null;
    id;

    constructor(cargo, id) {
        this.#cargo = cargo;
        this.id = id;
        this.texture = render.initCargoCell(id);
    }

    getItem() {
        return this.#cargo;
    }

    add(cargo) {
        this.#cargo = cargo;

        if (cargo !== null) {
            cargo.slotId = this.id;
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

    center() {
        render.addToCargoCell(this.#cargo.texture, this.texture);
    }
}