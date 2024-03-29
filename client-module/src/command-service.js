import * as initService from "./init-service.js";
import * as inventoryService from "./inventory-service.js";
import * as spaceItemService from "./obj-service/space-item-service.js";
import * as shipService from "./obj-service/ship-service.js";
import * as bulletService from "./obj-service/bullet-setvice.js";
import {Commands} from "./const/MessageCommand.js";

export function executeCommand(response) {
    let socketResponse;
    try {
        socketResponse = JSON.parse(response);
    } catch (e) {
        console.error("caught error when try to parse response", e, response);
        return;
    }

    let command = socketResponse.command;
    switch (command) {
        case Commands.ReceiveCharacter:
            initService.tryInitMotions(socketResponse.data);
            break;

        case Commands.ReceiveInventory:
            initService.tryInitInventory(socketResponse.data);
            break;

        case Commands.ReceiveSpaceObjects:
            shipService.createOrUpdate(socketResponse.data.characters);
            spaceItemService.createOrUpdate(socketResponse.data.itemsInSpace);
            bulletService.createOrUpdate(socketResponse.data.bullets);

            break;

        case Commands.LeavingPlayer:
            console.log(`character ${socketResponse.data} disconnected`)
            break;

        case Commands.ReceiveUpdateInventoryItem:
            inventoryService.updateItemSlot(socketResponse.data);
            break;

        case Commands.ReceiveWeaponInfo:
            break;

        case Commands.BlowUpCharacter:
            shipService.shipExplosion(socketResponse.data);
            break;

        case Commands.ReceiveItem:
            inventoryService.addItem(socketResponse.data);
            break;

        default:
            console.error("Unexpected command: " + command);
    }

}