import * as playerService from './character-service.js'
import {mainLogicInit} from "./main-logic.js";
import * as inventoryService from "./inventory-service.js";
import {initEngine} from "./render-engine.js";

let isPlayerInit = false;
const timerInterval = 250;
let tryCount;

export function startInitProcess() {
    tryCount = 10;
    const initLoop = setInterval(() => {
        if (isPlayerInit || tryCount < 0) {
            clearInterval(initLoop);
            console.log("Stopped character init loop")
        } else {
            console.log("Try to init character")
            playerService.sendInitPlayer();
            tryCount--;
        }
    }, timerInterval);
    console.log("Started character init loop")
}

export function initCharacter(data) {
    if (data == null) {
        console.log("Character.js not init");
    } else {
        isPlayerInit = true;
        initEngine();
        playerService.initMyCharacter(data.playerMotion.playerName, data.playerMotion);
        inventoryService.initInventory();

        mainLogicInit();
        console.log("My character init success");
    }
}