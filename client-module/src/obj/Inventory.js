import * as renderService from "../render-service.js";
import {CargoCell} from "./CargoCell.js";
import {EquipmentSlot} from "./EquipmentSlot.js";
import {EquipmentType} from "../const/EquipmentType.js";

export class Inventory {
    isOpen = false;
    cargoCells = [];
    engineSlot;
    fuelTankSlot;

    constructor() {
        renderService.createInventory();
        for (let i = 0; i < 6; i++) {
            let holdCell = new CargoCell(undefined, i);
            this.cargoCells.push(holdCell);
        }
        this.engineSlot = new EquipmentSlot(EquipmentType.Engine);
        this.fuelTankSlot = new EquipmentSlot(EquipmentType.FuelTank);
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