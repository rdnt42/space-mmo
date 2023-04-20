import {PixiEngine} from "./render-service-pixi.js";
import {ThreeEngine} from "./render-service-three.js";

export let renderEngine;

const engineType = "PIXI";

export function initEngine() {
    if (engineType === "PIXI") {
        renderEngine = new PixiEngine();
    } else if (engineType === "THREE") {
        renderEngine = new ThreeEngine();
    }
}
