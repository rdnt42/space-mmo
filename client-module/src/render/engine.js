import * as render from "../render/render.js";
import * as physics from "../render/physics.js";
import * as characterService from "../character-service.js";
import {shipsCfgMap} from "../cfg/images-cfg.js";

let ships = new Map();

export function createCharacter(characterName, shipTypeId) {
    let sprite = render.createCharacter(characterName, shipTypeId);
    sprite.position.set(250, 250);

    let cfg = shipsCfgMap.get(shipTypeId);
    let body = physics.createCharacter(sprite.x, sprite.y, cfg.polygons, cfg.scale);

    let staticCharSprite = render.createCharacter(characterName, 2);
    staticCharSprite.position.set(500, 500);
    let cfgStatic = shipsCfgMap.get(2);
    let staticCharBody = physics.createCharacter(staticCharSprite.x, staticCharSprite.y, cfgStatic.polygons, cfgStatic.scale);

    ships.set(characterName, [body, sprite]);
    ships.set("test", [staticCharBody, staticCharSprite]);
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

export function moveCharacter(character, speed, angle) {
    const angleInRadians = angle * (Math.PI / 180);
    const velX = Math.cos(angleInRadians) * speed / 100;
    const velY = Math.sin(angleInRadians) * speed / 100;

    let body = getBody(character.characterName);
    physics.moveBody(body, velX, velY, angleInRadians);

    let sprite = getSprite(character.characterName);
    render.renderCharacter(character.characterName, sprite, body.position.x, body.position.y, angle);
    character.movement.x = body.position.x;
    character.movement.y = body.position.y;
    character.movement.angle = angle;
    character.movement.speed = speed;
    syncCoords();

    // renderCharacter(characterName, velX, velY, angle);
}

function syncCoords() {
    for (let ship of ships.values()) {
        let body = ship[0];
        let sprite = ship[1];

        sprite.position.x = body.position.x;
        sprite.position.y = body.position.y;
        sprite.rotation = body.angle;
    }
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

