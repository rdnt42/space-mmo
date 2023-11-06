package marowak.dev.service.item;

import marowak.dev.dto.item.*;
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
                    .storageId(message.getStorageId())
                    .typeId(message.getTypeId())
                    .upgradeLevel(message.getUpgradeLevel())
                    .cost(message.getCost())
                    .name(message.getName())
                    .dsc(message.getDsc());

    public static final Function<EngineMessage, Item> engineMessageToItem = message ->
            ((Engine.EngineBuilder<?, ?>) messageToItemBuilder.apply(message, Engine.builder()))
                    .speed(message.getSpeed())
                    .jump(message.getJump())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final Function<FuelTankMessage, Item> fuelTankMessageToItem = message ->
            ((FuelTank.FuelTankBuilder<?, ?>) messageToItemBuilder.apply(message, FuelTank.builder()))
                    .capacity(message.getCapacity())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final Function<CargoHookMessage, Item> cargoHookMessageToItem = message ->
            ((CargoHook.CargoHookBuilder<?, ?>) messageToItemBuilder.apply(message, CargoHook.builder()))
                    .loadCapacity(message.getLoadCapacity())
                    .radius(message.getRadius())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

    public static final Function<HullMessage, Item> hullMessageToItem = message ->
            ((Hull.HullBuilder<?, ?>) messageToItemBuilder.apply(message, Hull.builder()))
                    .hp(message.getHp())
                    .evasion(message.getEvasion())
                    .armor(message.getArmor())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .config(message.getConfig())
                    .build();

    public static final Function<WeaponMessage, Item> weaponMessageToItem = message ->
            ((Weapon.WeaponBuilder<?, ?>) messageToItemBuilder.apply(message, Weapon.builder()))
                    .damage(message.getDamage())
                    .radius(message.getRadius())
                    .rate(message.getRate())
                    .damageTypeId(message.getDamageTypeId())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .build();

}
