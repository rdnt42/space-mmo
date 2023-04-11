import * as playerService from './character-service.js'
import * as render from "./render-service.js";
import {mainLogicInit} from "./main-logic.js";

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
        render.initRender();
        playerService.initMyCharacter(data.playerMotion.playerName, data.playerMotion);

        mainLogicInit();
        console.log("My character init success");
    }
}