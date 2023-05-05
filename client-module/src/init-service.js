import {mainLogicInit} from "./main-logic.js";
import * as inventoryService from "./inventory-service.js";
import {initEngine} from "./render/render-engine.js";
import {initKeyBoard} from "./keyboard-service.js";
import * as characterService from "./character-service.js";

let isMotionsInit = false;
let isInventoryInit = false;
const timerInterval = 250;
let tryCount;

let initData = {}

// TODO refactor
export function startInitMotions() {
    tryCount = 10;
    const initLoop = setInterval(() => {
        if (isMotionsInit || tryCount < 0) {
            clearInterval(initLoop);
            console.log("Stopped character init motions loop")
        } else {
            console.log("Try to init character motions")
            characterService.sendGetMotions();
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
            characterService.sendGetInventory();
            tryCount--;
        }
    }, timerInterval);
    console.log("Started character init inventory loop")
}

export function checkInit() {
    const initLoop = setInterval(() => {
        if (isInventoryInit && isMotionsInit) {
            clearInterval(initLoop);
            initEngine();

            let motion = initData.motion;
            characterService.initMyCharacter(motion.playerMotion.playerName, motion.playerMotion);
            characterService.updateOrCreateCharacters(motion);

            let inventory = initData.inventory;
            inventoryService.initInventory(inventory.slots, inventory.items);

            mainLogicInit();
            initKeyBoard();
            console.log("Character init success");
        }
    }, timerInterval);
}

export function tryInitMotions(data) {
    if (data == null) return;
    isMotionsInit = true;
    initData.motion = data;
}

export function tryInitInventory(data) {
    if (data == null) return;
    isInventoryInit = true;
    initData.inventory = data;
}