import * as socket from "./websocket-service.js";
import {PlayerEmptyRequest} from "./request/PlayerEmptyRequest.js";
import {CharacterMotionRequest} from "./request/CharacterRequest.js";
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

export function initMyCharacter(characterName, playerMotion) {
    createCharacter(characterName, playerMotion, 1);
    playerCharacterName = characterName;
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

export function updateOrCreateCharacters(data) {
    updateCharacter(data.playerMotion.playerName, data.playerMotion);

    //TODO add cleaner for charactersMap who not in response
    if(data.playersMotions === undefined) return;
    for (let playerMotion of data.playersMotions) {
        let character = charactersMap.get(playerMotion.playerName);
        if (character === undefined) {
            createCharacter(playerMotion.playerName, playerMotion, 1);
        } else {
            updateCharacter(playerMotion.playerName, playerMotion);
        }
    }
}

function updateCharacter(characterName, playerMotion) {
    let character = charactersMap.get(characterName);
    character.updateCharacter(playerMotion.x, playerMotion.y, playerMotion.angle, playerMotion.speed);
}

function createCharacter(characterName, playerMotion, shipTypeId) {
    let character = new Character(characterName);
    character.initCharacter(playerMotion.x, playerMotion.y, playerMotion.angle, playerMotion.speed, shipTypeId);

    charactersMap.set(characterName, character);
}