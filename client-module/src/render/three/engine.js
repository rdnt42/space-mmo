// create SceneManager
import {SceneManager} from "./sceneManager.js";

const canvas = document.getElementById("canvas");
const sceneManager = new SceneManager(canvas);

// handle DOM events
bindEventListeners();

// Render Loop
initEngine();


function bindEventListeners() {
    window.onresize = resizeCanvas;
    resizeCanvas();
}

function resizeCanvas() {
    canvas.style.width  = '100%';
    canvas.style.height = '100%';

    canvas.width  = canvas.offsetWidth;
    canvas.height = canvas.offsetHeight;

    sceneManager.onWindowResize();
}


function initEngine() {
    requestAnimationFrame(initEngine);
    sceneManager.update();
}




export class ThreeEngine {
    constructor() {
        initEngine();
    }

    createCharacter(characterName) {
    }

    createCharacterLabel(characterName) {
    }

    removeCharacter(characterName) {
    }

    createInventory() {
        return {};
    }

    initEquipment(equipmentType, idx) {
        return {};
    }

    initCargoCell(idx) {
        return {};
    }

    initEquipmentSlot(equipmentType) {
        return {};
    }

    addToEquipmentSlot(equipment, slot) {
    }

    removeFromEquipmentSlot(equipment) {
    }

    addToCargoCell(cargo, hold) {
    }

    removeFromCargoCell(cargo) {
    }

    changeStateInventory(state) {
    }

}