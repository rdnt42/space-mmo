import {getRelativeX, getRelativeY} from "./obj-utils.js";
import {Ship} from "../obj/Ship.js";

let ships = new Map();
let characterName;

export function createOrUpdate(objs) {
    for (const obj of objs) {
        createOrUpdateObj(obj);
    }
}

function createOrUpdateObj(obj) {
    const {characterName, shipTypeId, x, y, angle, speed, polygon} = obj;
    let abs = getPlayerShip().movement;
    let newX = getRelativeX(x, abs.x);
    let newY = getRelativeY(y, abs.y);

    let ship = ships.get(characterName);
    if (ship === undefined) {
        ship = new Ship(characterName, x, y, angle, speed, shipTypeId, polygon);
        ships.set(ship.characterName, ship);
    } else {
        ship.updateObj(newX, newY);
    }

    ship.renderObj();
}

export function createPlayerShip(data) {
    const {shipTypeId, x, y, angle, speed, polygon} = data;
    const ship = new Ship(data.characterName, x, y, angle, speed, shipTypeId, polygon);
    characterName = ship.characterName;
    ships.set(ship.characterName, ship);
    ship.renderObj();
}

export function getPlayerShip() {
    return ships.get(characterName);
}

export function shipExplosion(characterName) {
    const ship = ships.get(characterName);
    ship.blowUp();
    ships.delete(characterName);
}