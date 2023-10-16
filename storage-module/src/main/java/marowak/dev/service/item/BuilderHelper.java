package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.entity.*;
import marowak.dev.service.TriFunction;
import message.*;

public class BuilderHelper {

    private BuilderHelper() {
    }

    // TODO common builder for item like in mechanic -> builderHelper
    public static final TriFunction<Engine, Item, ItemMessageKey, ItemMessage> engineToMessage =
            (engine, item, key) -> EngineMessage.builder()
                    .key(key)
                    .id(engine.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .speed(engine.speed())
                    .jump(engine.jump())
                    .equipmentTypeId(engine.engineTypeId())
                    .build();

    public static final TriFunction<FuelTank, Item, ItemMessageKey, ItemMessage> fuelTankToMessage =
            (fuelTank, item, key) -> FuelTankMessage.builder()
                    .key(key)
                    .id(fuelTank.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .capacity(fuelTank.capacity())
                    .equipmentTypeId(fuelTank.fuelTankTypeId())
                    .build();

    public static final TriFunction<CargoHook, Item, ItemMessageKey, ItemMessage> cargoHookToMessage =
            (cargoHook, item, key) -> CargoHookMessage.builder()
                    .key(key)
                    .id(cargoHook.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .loadCapacity(cargoHook.loadCapacity())
                    .radius(cargoHook.radius())
                    .equipmentTypeId(cargoHook.cargoHookTypeId())
                    .build();

    public static final TriFunction<Hull, Item, ItemMessageKey, ItemMessage> hullToMessage =
            (hull, item, key) -> HullMessage.builder()
                    .key(key)
                    .id(hull.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .hp(hull.hp())
                    .evasion(hull.evasion())
                    .armor(hull.armor())
                    .equipmentTypeId(hull.hullTypeId())
                    .config(hull.config())
                    .build();

    public static final TriFunction<Weapon, Item, ItemMessageKey, ItemMessage> weaponToMessage =
            (weapon, item, key) -> WeaponMessage.builder()
                    .key(key)
                    .id(weapon.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .damage(weapon.damage())
                    .radius(weapon.radius())
                    .rate(weapon.rate())
                    .damageTypeId(weapon.damageTypeId())
                    .equipmentTypeId(weapon.weaponTypeId())
                    .build();
}
