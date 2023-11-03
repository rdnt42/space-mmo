import * as socket from "./websocket-service.js";
import {PlayerEmptyRequest} from "./message/PlayerEmptyRequest.js";
import {CharacterMotionRequest, CharacterShootingRequest} from "./message/CharacterMessage.js";
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

export function initMyCharacter(data) {
    playerCharacterName = data.characterName;
    createCharacter(data);
}

export function getPlayerCharacter() {
    return charactersMap.get(playerCharacterName);
}

export function sendMotion(forceTypeId, angle, isUpdate) {
    const request = new CharacterMotionRequest(isUpdate, forceTypeId, angle);
    socket.sendMessage(request);
}

export function sendShooting(isShooting, angle) {
    const request = new CharacterShootingRequest(isShooting, angle, Date.now());
    socket.sendMessage(request);
}

export function updateCharacterData(state) {
    let character = charactersMap.get(state.characterName);
    if (character === undefined) {
        createCharacter(state);
    } else {
        character.updateCharacter(state.x, state.y, state.angle, state.speed);
    }
}

export function updateCharactersData(states) {
    for (const state of states) {
        updateCharacterData(state)
    }
}

function createCharacter(data) {
    let character = new Character(data.characterName);
    character.initCharacter(data.x, data.y, data.angle, data.speed, data.shipTypeId, data.polygon);

    charactersMap.set(data.characterName, character);
    console.log(`create character ${character.characterName}, ship: ${data.shipTypeId}`)
}

export function characterExplosion(characterName) {
    const character = charactersMap.get(characterName);
    character.blowUp();
    charactersMap.delete(characterName);
}