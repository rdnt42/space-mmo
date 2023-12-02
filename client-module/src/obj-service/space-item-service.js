import {SpaceItem} from "../obj/SpaceItem.js";
import {getRelativeX, getRelativeY} from "./obj-utils.js";
import * as shipService from "./ship-service.js";

let spaceItems = new Map();

export function createOrUpdate(objs) {
    for (const obj of objs) {
        createOrUpdateObj(obj);
    }
}

function createOrUpdateObj(obj) {
    const {id, x, y, itemTypeId, dsc, name} = obj;
    let abs = shipService.getPlayerShip().movement;
    let newX = getRelativeX(x, abs.x);
    let newY = getRelativeY(y, abs.y);

    let item = spaceItems.get(id);
    if (item === undefined) {
        item = new SpaceItem(id, newX, newY, itemTypeId, dsc, name)
        spaceItems.set(item.id, item);
    } else {
        item.updateObj(newX, newY);
    }

    item.renderObj();
}

export function getObjectsMap() {
    return spaceItems;
}