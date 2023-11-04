import {EquipmentTypeId} from "./const/EquipmentTypeId.js";
import {EquipmentSlot} from "./obj/EquipmentSlot.js";
import {CargoCell} from "./obj/CargoCell.js";

export function inHull(item) {
    return item.storageId === EquipmentSlot.storageId;
}

export function inCargo(item) {
    return item.storageId === CargoCell.storageId;
}

export function isWeapon(item) {
    return item.typeId >= EquipmentTypeId.Weapon1 && item.typeId <= EquipmentTypeId.Weapon5;
}