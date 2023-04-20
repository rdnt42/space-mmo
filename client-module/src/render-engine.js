import {PixiEngine} from "./render-service-pixi.js";
import {ThreeEngine} from "./render/three/engine.js";

export let renderEngine;

const engineType = "THREE";

export function initEngine() {
    if (engineType === "PIXI") {
        renderEngine = new PixiEngine();
    } else if (engineType === "THREE") {
        renderEngine = new ThreeEngine();
    }
}
