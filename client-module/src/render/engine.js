import * as render from "../render/render.js";
import * as characterService from "../character-service.js";

let ships = new Map();

export function createCharacter(characterName, shipTypeId, x, y, angle, polygon) {
    let sprite = render.createCharacter(characterName, shipTypeId, x, y, angle, polygon);
    ships.set(characterName, sprite);
}

export function renderCharacter(characterName, x, y, angle) {
    let ship = ships.get(characterName);

    let abs = characterService.getPlayerCharacter().movement;
    let newX = getX(x, abs.x);
    let newY = getY(y, abs.y);

    render.renderCharacter(characterName, ship, newX, newY, angle);
    ship.isUpdated = true;
}

export function removeCharacter(characterName) {
    let sprite = ships.get(characterName);
    render.removeCharacter(characterName, sprite);
    ships.delete(characterName)
}

export function getRenderCoords(characterName) {
    let sprite = ships.get(characterName);
    return render.getRenderCoords(sprite);
}

function getX(currX, diffX) {
    return currX - diffX + window.innerWidth / 2;
}

function getY(currY, diffY) {
    return currY - diffY + window.innerHeight / 2;
}

export function blowUpCharacter(characterName) {
    let sprite = ships.get(characterName);
    let movement = characterService.getCharacter(characterName).movement;
    render.blowUpCharacter(sprite, movement.x, movement.y);
    removeCharacter(characterName);
}


