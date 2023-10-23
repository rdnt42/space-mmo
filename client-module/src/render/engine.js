import * as render from "../render/render.js";
import * as characterService from "../character-service.js";

let ships = new Map();
let bullets = new Map();

export function createCharacter(characterName, shipTypeId) {
    let sprite = render.createCharacter(characterName, shipTypeId);
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

export function createOrUpdateBullet(id, x, y, angle, type) {
    let abs = characterService.getPlayerCharacter().movement;
    let newX = getX(x, abs.x);
    let newY = getY(y, abs.y);
    let bullet = bullets.get(id);
    if (bullet === undefined) {
        bullet = render.createBullet(newX, newY, angle, type);
        bullets.set(id, bullet);
    }

    render.renderBullet(bullet, newX, newY, angle);
    bullet.isUpdated = true;
}

function getX(currX, diffX) {
    return currX - diffX + window.innerWidth / 2;
}

function getY(currY, diffY) {
    return currY - diffY + window.innerHeight / 2;
}

export function clearUnusedObjects() {
    removeOrMarkObjects(bullets);
}

function removeOrMarkObjects(map) {
    map.forEach((value, key, map) => {
        if (value.isUpdated === false) {
            render.removeSprite(value);
            map.delete(key);
        }
        value.isUpdated = false;
    });
}

