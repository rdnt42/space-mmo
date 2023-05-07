import {Inventory} from "./obj/Inventory.js";
import {Item} from "./obj/Item.js";
import {EquipmentSlotId} from "./const/EquipmentSlotId.js";

let inventory;

export function initInventory(slots, items) {
    inventory = new Inventory(slots);
    for (const itemSrc of items) {
        let it = new Item(itemSrc);
        inventory.addInitItem(it);
    }

    document.addEventListener("keydown", (event) => {
        event.preventDefault();
        if (event.key === "i") {
            inventory.changeState();
        }
    });
}

export function doubleClickCallback(texture) {
    if (texture.textureParentObj instanceof Item) {
        inventory.changeEquipmentSlot(texture.textureParentObj);
    }
}

export function dragEndCallback(texture) {
    let item = texture.textureParentObj;
    let newIdx;
    for (const cell of inventory.cargoCells) {
        if (cell.getCargo() === item) {
            newIdx = cell.idx;
            continue;
        }
        let hasCollision = hasHalfCollision(texture, cell.texture);
        if (hasCollision) {
            newIdx = cell.idx;
            break;
        }
    }
    console.log(`idx after dragging ${newIdx}`);
    inventory.addCargoBySlot(item, newIdx);
}

function hasHalfCollision(r1, r2) {
    let r1Bounds = r1.getBounds();
    let r2Bounds = r2.getBounds();

    return r1Bounds.x + r1Bounds.width * 0.5 > r2Bounds.x &&
        r1Bounds.x < r2Bounds.x + r2Bounds.width * 0.5 &&
        r1Bounds.y + r1Bounds.height * 0.5 > r2Bounds.y &&
        r1Bounds.y < r2Bounds.y + r2Bounds.height * 0.5;
}

export function getEngine() {
    return inventory.getEquipment(EquipmentSlotId.Engine);
}

