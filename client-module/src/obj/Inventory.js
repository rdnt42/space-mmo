import * as renderService from "../render-service.js";

export class Inventory {
    isOpen = false;

    constructor(){
    }

    initInventory() {
        renderService.createInventory();
    }

    changeState() {
        this.isOpen = !this.isOpen;
        renderService.changeStateInventory(this.isOpen);
    }
}