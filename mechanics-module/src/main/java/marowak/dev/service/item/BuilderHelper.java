package marowak.dev.service.item;

import marowak.dev.dto.item.*;
import marowak.dev.request.ItemUpdate;
import marowak.dev.service.TriFunction;
import message.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BuilderHelper {

    private BuilderHelper() {
    }

    public static final BiFunction<ItemMessage, Item.ItemBuilder<?, ?>, Item.ItemBuilder<?, ?>> messageToItemBuilder =
            (message, baseBuilder) -> baseBuilder
                    .id(message.getId())
                    .slotId(message.getSlotId())
                    .typeId(message.getTypeId())
                    .upgradeLevel(message.getUpgradeLevel())
                    .cost(message.getCost())
                    .name(message.getName())
                    .dsc(message.getDsc());

    public static final TriFunction<Item, ItemUpdate, Item.ItemBuilder<?, ?>, Item.ItemBuilder<?, ?>> itemToItemBuilder =
            (engine, request, baseBuilder) -> baseBuilder
                    .id(engine.getId())
                    .slotId(request.slotId())
                    .typeId(engine.getTypeId())
                    .upgradeLevel(engine.getUpgradeLevel())
                    .cost(engine.getCost())
                    .name(engine.getName())
                    .dsc(engine.getDsc());
    public static final Function<EngineMessage, Item> engineMessageToItem = message ->
            ((Engine.EngineBuilder<?, ?>) messageToItemBuilder.apply(message, Engine.builder()))
                    .speed(message.getSpeed())
                    .jump(message.getJump())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final BiFunction<Engine, ItemUpdate, Engine> engineToNewEngine =
            (engine, request) -> ((Engine.EngineBuilder<?, ?>) itemToItemBuilder.apply(engine, request, Engine.builder()))
                    .speed(engine.getSpeed())
                    .jump(engine.getJump())
                    .equipmentTypeId(engine.getEquipmentTypeId())
                    .build();

    public static final Function<FuelTankMessage, Item> fuelTankMessageToItem = message ->
            ((FuelTank.FuelTankBuilder<?, ?>) messageToItemBuilder.apply(message, FuelTank.builder()))
                    .capacity(message.getCapacity())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final BiFunction<FuelTank, ItemUpdate, FuelTank> tankToNewTank =
            (fuelTank, request) -> ((FuelTank.FuelTankBuilder<?, ?>) itemToItemBuilder.apply(fuelTank, request, FuelTank.builder()))
                    .capacity(fuelTank.getCapacity())
                    .equipmentTypeId(fuelTank.getEquipmentTypeId())
                    .build();

    public static final Function<CargoHookMessage, Item> cargoHookMessageToItem = message ->
            ((CargoHook.CargoHookBuilder<?, ?>) messageToItemBuilder.apply(message, CargoHook.builder()))
                    .loadCapacity(message.getLoadCapacity())
                    .radius(message.getRadius())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final BiFunction<CargoHook, ItemUpdate, CargoHook> cargoHookToNewHook =
            (cargoHook, request) -> ((CargoHook.CargoHookBuilder<?, ?>) itemToItemBuilder.apply(cargoHook, request, CargoHook.builder()))
                    .loadCapacity(cargoHook.getLoadCapacity())
                    .radius(cargoHook.getRadius())
                    .equipmentTypeId(cargoHook.getEquipmentTypeId())
                    .build();

    public static final Function<HullMessage, Item> hullMessageToItem = message ->
            ((Hull.HullBuilder<?, ?>) messageToItemBuilder.apply(message, Hull.builder()))
                    .hp(message.getHp())
                    .evasion(message.getEvasion())
                    .armor(message.getArmor())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .config(message.getConfig())
                    .build();

    public static final BiFunction<Hull, ItemUpdate, Hull> hullToNewHull =
            (hull, request) -> ((Hull.HullBuilder<?, ?>) itemToItemBuilder.apply(hull, request, Hull.builder()))
                    .hp(hull.getHp())
                    .evasion(hull.getEvasion())
                    .armor(hull.getArmor())
                    .equipmentTypeId(hull.getEquipmentTypeId())
                    .config(hull.getConfig())
                    .build();

    public static final Function<WeaponMessage, Item> weaponMessageToItem = message ->
            ((Weapon.WeaponBuilder<?, ?>) messageToItemBuilder.apply(message, Weapon.builder()))
                    .damage(message.getDamage())
                    .radius(message.getRadius())
                    .damageTypeId(message.getDamageTypeId())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final BiFunction<Weapon, ItemUpdate, Weapon> weaponToNewWeapon =
            (weapon, request) -> ((Weapon.WeaponBuilder<?, ?>) itemToItemBuilder.apply(weapon, request, Weapon.builder()))
                    .equipmentTypeId(weapon.getEquipmentTypeId())
                    .build();
}
