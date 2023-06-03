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

export function initMyCharacter(response) {
    playerCharacterName = response.characterName;
    createCharacter(response);
}

export function getPlayerCharacter() {
    return charactersMap.get(playerCharacterName);
}

export function sendMotion(forceTypeId, angle, isUpdate) {
    const request = new CharacterMotionRequest(isUpdate, forceTypeId, angle, Date.now());
    socket.sendMessage(request);
}

export function sendShooting(isShooting, angle) {
    const request = new CharacterShootingRequest(isShooting, angle, Date.now());
    socket.sendMessage(request);
}

export function updateCharacterData(data) {
    let character = charactersMap.get(data.characterName);
    if (character === undefined) {
        createCharacter(data);
    } else {
        character.updateCharacter(data.x, data.y, data.angle, data.speed);
    }
}

function createCharacter(response) {
    let character = new Character(response.characterName);
    character.initCharacter(response.x, response.y, response.angle, response.speed, response.shipTypeId);

    charactersMap.set(response.characterName, character);
    console.log(`create character ${character.characterName}, ship: ${response.shipTypeId}`)
}

// TODO
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