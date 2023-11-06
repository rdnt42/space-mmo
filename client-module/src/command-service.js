import * as initService from "./init-service.js";
import * as characterService from "./character-service.js";
import * as inventoryService from "./inventory-service.js";
import * as spaceItemService from "./space-item-service.js";
import * as weaponService from "./weapon-service.js";
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
        case Commands.GetMotions:
            initService.tryInitMotions(socketResponse.data);
            break;

        case Commands.GetInventory:
            initService.tryInitInventory(socketResponse.data);
            break;

        case Commands.UpdateMotion:
            characterService.updateCharactersData(socketResponse.data.characters);
            spaceItemService.updateSpaceItem(socketResponse.data.itemsInSpace);
            break;

        case Commands.LeavingPlayer:
            console.log(`character ${socketResponse.data} disconnected`)
            break;

        case Commands.UpdateInventoryItem:
            inventoryService.updateItemSlot(socketResponse.data);
            break;

        case Commands.UpdateShooting:
            weaponService.updateBulletData(socketResponse.data);
            break;

        case Commands.BlowUpCharacter:
            characterService.characterExplosion(socketResponse.data);
            break;

        default:
            console.error("Unexpected command: " + command);
    }

}