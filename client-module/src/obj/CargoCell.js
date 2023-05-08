import * as render from "../render/render.js";
import * as socket from "../websocket-service.js";
import {CharacterItemRequest} from "../request/CharacterRequest.js";

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
        this.#cargo = cargo;

        if (cargo !== undefined) {
            render.addToCargoCell(cargo.texture, this.texture);
            cargo.slotId = this.idx;
            socket.sendMessage(new CharacterItemRequest(cargo.id, cargo.slotId));
        }
    }

    removeFromCargoCell() {
        if (this.#cargo === undefined) return undefined;

        let removedCargo = this.#cargo;
        this.#cargo = undefined;
        render.removeFromCargoCell(removedCargo.texture);
        removedCargo.slotId = null;


        return removedCargo;
    }

    swapCargo(newCell) {
        let newItem = newCell.getCargo();
        newCell.addToCargoCell(this.#cargo);
        this.addToCargoCell(newItem);
    }

    center() {
        render.addToCargoCell(this.#cargo.texture, this.texture);
    }
}