import * as socket from "./websocket-service.js";
import {PlayerInitRequest} from "./request/PlayerInitRequest.js";
import {PlayerMotionRequest} from "./request/PlayerRequest.js";
import {Character} from "./obj/Character.js";

let playerCharacterName;
let charactersMap = new Map();

export function sendInitPlayer() {
    socket.sendMessage(new PlayerInitRequest());
}

export function initMyCharacter(characterName, playerMotion) {
    createCharacter(characterName, playerMotion);
    playerCharacterName = characterName;
}

export function getPlayerCharacter() {
    return charactersMap.get(playerCharacterName);
}

export function sendMotion(speed, angle, isUpdate) {
    const request = new PlayerMotionRequest(isUpdate, speed, angle);
    socket.sendMessage(request);
}

export function renderCharacters() {
    for (let character of charactersMap.values()) {
        character.render();
    }
}

export function updateOrCreateCharacters(data) {
    updateCharacter(data.playerMotion.playerName, data.playerMotion);

    // TODO add cleaner for charactersMap who not in response
    for (let playerMotion of data.playersMotions) {
        let character = charactersMap.get(playerMotion.playerName);
        if (character === undefined) {
            createCharacter(playerMotion.playerName, playerMotion);
        } else {
            updateCharacter(playerMotion.playerName, playerMotion);
        }
    }
}

function updateCharacter(characterName, playerMotion) {
    let character = charactersMap.get(characterName);
    character.updateCharacter(playerMotion.x, playerMotion.y, playerMotion.angle, playerMotion.speed);
}

function createCharacter(characterName, playerMotion) {
    let character = new Character(characterName);
    character.initCharacter(playerMotion.x, playerMotion.y, playerMotion.angle, playerMotion.speed);

    charactersMap.set(characterName, character);
}