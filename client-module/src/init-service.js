import * as playerService from './character-service.js'
import {mainLogicInit} from "./main-logic.js";
import * as inventoryService from "./inventory-service.js";
import {initEngine} from "./render/render-engine.js";

let isMotionsInit = false;
let isInventoryInit = false;
const timerInterval = 250;
let tryCount;

// TODO refactor
export function startInitMotions() {
    tryCount = 10;
    const initLoop = setInterval(() => {
        if (isMotionsInit || tryCount < 0) {
            clearInterval(initLoop);
            console.log("Stopped character init motions loop")
        } else {
            console.log("Try to init character motions")
            playerService.sendGetMotions();
            tryCount--;
        }
    }, timerInterval);
    console.log("Started character init motions loop")
}

export function startInitInventory() {
    tryCount = 10;
    const initLoop = setInterval(() => {
        if (isInventoryInit || tryCount < 0) {
            clearInterval(initLoop);
            console.log("Stopped character init inventory loop")
        } else {
            console.log("Try to init character inventory")
            playerService.sendGetInventory();
            tryCount--;
        }
    }, timerInterval);
    console.log("Started character init inventory loop")
}

export function checkInit() {
    const initLoop = setInterval(() => {
        if (isInventoryInit && isMotionsInit) {
            clearInterval(initLoop);
            console.log("All data init");
            mainLogicInit();
        }
    }, timerInterval);
}

export function initMotions(data) {
    if (data != null) {
        isMotionsInit = true;
        initEngine();
        playerService.initMyCharacter(data.playerMotion.playerName, data.playerMotion);

        console.log("My character init success");
    }
}

export function initInventory(data) {
    if (data != null) {
        isInventoryInit = true;
        initEngine();
        inventoryService.initInventory(data.slots, data.items, data.cargos);

        console.log("My character init success");
    }
}