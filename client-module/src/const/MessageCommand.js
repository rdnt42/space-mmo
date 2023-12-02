export const Commands = {
    // SEND
    GetCharacter: "CMD_GET_CHARACTER",
    GetInventory: "CMD_GET_INVENTORY",
    UpdateCharacterPosition: "CMD_UPDATE_CHARACTER_POSITION",
    UpdateInventoryItem: "CMD_UPDATE_INVENTORY_ITEM",
    UpdateMotion: "CMD_UPDATE_MOTION",
    UpdateShooting: "CMD_UPDATE_SHOOTING",
    TakeItemFromSpace: "CMD_TAKE_ITEM_FROM_SPACE",

    // RECEIVE
    ReceiveCharacter: "CMD_RECEIVE_CHARACTER",
    ReceiveInventory: "CMD_RECEIVE_INVENTORY",
    ReceiveSpaceObjects: "CMD_RECEIVE_SPACE_OBJECTS",
    ReceiveUpdateInventoryItem: "CMD_RECEIVE_UPDATE_INVENTORY_ITEM",
    ReceiveWeaponInfo: "CMD_RECEIVE_WEAPON_INFO",
    ReceiveItem: "CMD_RECEIVE_ITEM",

    LeavingPlayer: "CMD_LEAVING_PLAYER",
    BlowUpCharacter: "CMD_BLOW_UP_CHARACTER",
}