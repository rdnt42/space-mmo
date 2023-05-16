import * as main from "./main-logic.js";
import * as inventoryService from "./inventory-service.js";
import * as renderEngine from "./render/render.js";
import * as keyboard from "./keyboard-service.js";
import * as characterService from "./character-service.js";
import {CharacterResponse} from "./message/CharacterMessage.js";

const timerInterval = 100;
let timers = new Map();
const funcs = {
    characters: "Characters",
    inventory: "Inventory"
}

export function startInitAll() {
    renderEngine.initEngine();

    initBefore(characterService.sendGetMotions, funcs.characters);
    initBefore(characterService.sendGetInventory, funcs.inventory);
}

function initBefore(func, funcName) {
    let attempt = 0;
    const maxAttempt = 5;

    let timerId = setInterval(() => {
        console.log(`attempt: ${attempt} for function: ${funcName}`);
        if (attempt > maxAttempt) {
            console.log(`initialization for function: ${funcName} stopped due to exceeded retries`);
        } else {
            console.log(`try init function: ${funcName}`);
            attempt++;
            func();
        }
    }, timerInterval);

    timers.set(funcName, timerId);
}

function initAfter() {
    if (timers.size > 0) return;

    main.mainLogicInit();
    keyboard.initKeyBoard();
    renderEngine.startEngineTimer();
    console.log("Client init success");
}

export function tryInitMotions(data) {
    if (data == null) return;

    stopTimer(funcs.characters);
    let response = new CharacterResponse(data);
    characterService.initMyCharacter(response);
    characterService.updateOrCreateCharacters(data);
    removeTimer(funcs.characters);
}

export function tryInitInventory(data) {
    if (data == null) return;

    stopTimer(funcs.inventory);
    inventoryService.initInventory(data);
    removeTimer(funcs.inventory);
}

function stopTimer(key) {
    let timer = timers.get(key);
    clearInterval(timer);
}

function removeTimer(key) {
    timers.delete(key);
    initAfter();
}