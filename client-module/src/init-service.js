import * as character from './character-service.js'
import * as motion from "./motion-service.js";
import * as render from "./render-service.js";

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
            character.sendInitCharacter();
            tryCount--;
        }
    }, timerInterval);
    console.log("Started character init loop")
}

export function initCharacter(character) {
    if (character == null) {
        console.log("Character not init");
    } else {
        isPlayerInit = true;
        motion.update(character);
        render.updateAllPlayers(character);
        render.initRender();
        motion.keyBoardInit();
        console.log("Character init success");
    }
}