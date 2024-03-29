import * as main from "./main-logic.js";
import * as inventoryService from "./inventory-service.js";
import * as renderEngine from "./render/render.js";
import * as keyboard from "./keyboard-service.js";
import * as shipService from "./obj-service/ship-service.js";
import * as socket from "./websocket-service.js";
import {PlayerEmptyRequest} from "./message/PlayerEmptyRequest.js";
import {Commands} from "./const/MessageCommand.js";

const timerInterval = 100;
let timers = new Map();
const funcs = {
    characters: "Characters",
    inventory: "Inventory"
}

export function startInitAll() {
    renderEngine.initRender();

    initBefore(sendGetCharacter, funcs.characters);
    initBefore(sendGetInventory, funcs.inventory);
}

function sendGetCharacter() {
    socket.sendMessage(new PlayerEmptyRequest(Commands.GetCharacter));
}

function sendGetInventory() {
    socket.sendMessage(new PlayerEmptyRequest(Commands.GetInventory));
}

function initBefore(func, funcName) {
    let attempt = 0;
    const maxAttempt = 5;

    let timerId = setInterval(() => {
        console.log(`attempt: ${attempt} for function: ${funcName}`);
        if (attempt > maxAttempt) {
            console.log(`initialization for function: ${funcName} stopped due to exceeded retries`);
            clearInterval(timerId);
        } else {
            console.log(`try init function: ${funcName}`);
            attempt++;
            func();
        }
    }, timerInterval);

    timers.set(funcName, timerId);
}

let isInit = false;
function initAfter() {
    if (timers.size > 0) return;
    if (isInit) return;
    isInit = true;

    main.mainLogicInit();
    keyboard.initKeyBoard();
    renderEngine.startEngineTimer();
    console.log("Client init success");
}

export function tryInitMotions(data) {
    if (data == null || timers.get(funcs.characters) === undefined) return;

    stopTimer(funcs.characters);
    shipService.createPlayerShip(data);

    removeTimer(funcs.characters);
}

export function tryInitInventory(data) {
    if (data == null || timers.get(funcs.inventory) === undefined) return;

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