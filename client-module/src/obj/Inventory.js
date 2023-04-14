import * as renderService from "../render-service.js";
import {CargoCell} from "./CargoCell.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];

    constructor() {
        renderService.createInventory();
        for (let i = 0; i < 6; i++) {
            let holdCell = new CargoCell(undefined, i);
            this.cargoCells.push(holdCell);
        }
    }

    addCargo(cargo) {
        for (let cargoCell of this.cargoCells) {
            if (cargoCell.cargo === undefined) {
                renderService.addToCargo(cargo, cargoCell, true);
                cargoCell.cargo = cargo;
                break;
            }
        }
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderService.changeStateInventory(this.isOpen);
    }
}