import {PixiEngine} from "./render-service-pixi.js";

export let renderEngine;

export function initEngine() {
    renderEngine = new PixiEngine();
}
