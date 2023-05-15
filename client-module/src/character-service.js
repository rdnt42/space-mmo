import * as socket from "./websocket-service.js";
import {PlayerEmptyRequest} from "./request/PlayerEmptyRequest.js";
import {CharacterMotionRequest, CharacterResponse} from "./request/CharacterRequest.js";
import {Character} from "./obj/Character.js";
import {Commands} from "./const/MessageCommand.js";

let playerCharacterName;
let charactersMap = new Map();

export function sendGetMotions() {
    socket.sendMessage(new PlayerEmptyRequest(Commands.GetMotions));
}

export function sendGetInventory() {
    socket.sendMessage(new PlayerEmptyRequest(Commands.GetInventory));
}

export function initMyCharacter(response) {
    createCharacter(response);
    playerCharacterName = response.characterName;
}

export function getPlayerCharacter() {
    return charactersMap.get(playerCharacterName);
}

export function sendMotion(speed, angle, isUpdate) {
    const request = new CharacterMotionRequest(isUpdate, speed, angle, Date.now());
    socket.sendMessage(request);
}

export function getAllCharacters() {
    return charactersMap;
}

export function updateCharactersData(data) {
    let response = new CharacterResponse(data);
    updateOrCreateCharacters(response);
    removeUnusedCharacters(response);
}

export function updateOrCreateCharacters(response) {
    let character = charactersMap.get(response.characterName);
    if (character === undefined) {
        createCharacter(response);
    } else {
        updateCharacter(response);
    }
}

function updateCharacter(response) {
    let character = charactersMap.get(response.characterName);
    character.updateCharacter(response.x, response.y, response.angle, response.speed);
}

function createCharacter(response) {
    let character = new Character(response.characterName);
    character.initCharacter(response.x, response.y, response.angle, response.speed, response);

    charactersMap.set(response.characterName, character);
}

function removeUnusedCharacters(data) {
    // it doesn't work in flux
    return;

    if (charactersMap.size - 1 === data.playersMotions.length) return;

    let newChars = new Set();
    for (let motion of data.playersMotions) {
        newChars.add(motion.playerName);
    }

    for (let character of charactersMap.values()) {
        if (character.characterName !== playerCharacterName && !newChars.has(character)) {
            character.destroy();
            charactersMap.delete(character.characterName);
            console.log(`player ${character.characterName} deleted from map`)
        }
    }

}