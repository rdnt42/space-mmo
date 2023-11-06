import * as renderEngine from "./render/engine.js";

export function updateSpaceItem(items) {
    for (const item of items) {
        renderEngine.createOrUpdateSpaceItem(item.id, item.coords.x, item.coords.y, item.itemTypeId)
    }
}