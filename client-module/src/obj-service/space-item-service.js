import * as characterService from "../character-service.js";
import {SpaceItem} from "../obj/SpaceItem.js";
import {getRelativeX, getRelativeY} from "./obj-utils.js";

let spaceItems = new Map();

export function createOrUpdate(objs) {
    for (const obj of objs) {
        createOrUpdateObj(obj.id, obj.coords.x, obj.coords.y, obj.itemTypeId)
    }
}

function createOrUpdateObj(id, x, y, itemTypeId, dsc) {
    let abs = characterService.getPlayerCharacter().movement;
    let newX = getRelativeX(x, abs.x);
    let newY = getRelativeY(y, abs.y);

    let item = spaceItems.get(id);
    if (item === undefined) {
        item = new SpaceItem(id, newX, newY, itemTypeId, dsc)
        spaceItems.set(item.id, item);
    } else {
        item.updateObj(newX, newY);
    }

    item.renderObj();
}