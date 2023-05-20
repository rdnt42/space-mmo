import * as render from "../render/render.js";
import * as physics from "../render/physics.js";
import * as characterService from "../character-service.js";

let ships = new Map();

export function createCharacter(characterName, shipTypeId) {
    let sprite = render.createCharacter(characterName, shipTypeId);
    let body = physics.createCharacter(sprite);

    let arr = [];
    arr.push(body);
    arr.push(sprite);
    ships.set(characterName, arr)
}

export function renderCharacter(characterName, x, y, angle) {
    let body = getBody(characterName);
    let sprite = getSprite(characterName);

    let abs = characterService.getPlayerCharacter().movement;
    let newX = getX(x, abs.x);
    let newY = getY(y, abs.y);

    physics.renderBody(body, newX, newY);
    render.renderCharacter(characterName, sprite, newX, newY, angle);
}

export function removeCharacter(characterName) {
    let sprite = getSprite(characterName);
    render.removeCharacter(characterName, sprite);

    let body = getBody(characterName);
    physics.removeBody(body);
}

function getBody(characterName) {
    let obj = ships.get(characterName);
    if (obj === undefined) {
        console.error(`character ${characterName} doesn't exists`)
    }

    return obj[0];
}

function getSprite(characterName) {
    let obj = ships.get(characterName);
    if (obj === undefined) {
        console.error(`character ${characterName} doesn't exists`)
    }

    return obj[1];
}

function getX(currX, diffX) {
    return currX - diffX + window.innerWidth / 2;
}

function getY(currY, diffY) {
    return currY - diffY + window.innerHeight / 2;
}

