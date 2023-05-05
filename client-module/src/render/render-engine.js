import {PixiEngine} from "./pixi/engine.js";
import {ThreeEngine} from "./three/engine.js";

export let renderEngine;

const typePixi = "PIXI";
const typeThree = "THREE";

const engineType = typePixi;

export function initEngine() {
    console.log(`engine use: ${engineType}`)
    if (engineType === typePixi) {
        renderEngine = new PixiEngine();
    } else if (engineType === typeThree) {
        renderEngine = new ThreeEngine();
    }
}

export function startEngineTimer() {
    renderEngine.startEngineTimer();
}
