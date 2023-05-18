package marowak.dev.service.item;

import marowak.dev.dto.item.*;
import marowak.dev.request.ItemUpdate;
import message.CargoHookMessage;
import message.EngineMessage;
import message.FuelTankMessage;
import message.HullMessage;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BuilderHelper {

    private BuilderHelper() {
    }

    public static final Function<EngineMessage, Item> engineMessageToItem = message -> Engine.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .typeId(message.getTypeId())
            .upgradeLevel(message.getUpgradeLevel())
            .cost(message.getCost())
            .name(message.getName())
            .dsc(message.getDsc())
            .speed(message.getSpeed())
            .jump(message.getJump())
            .equipmentTypeId(message.getEquipmentTypeId())
            .build();

    public static final BiFunction<Engine, ItemUpdate, Engine> engineToNewEngine =
            (engine, request) -> Engine.builder()
                    .id(engine.getId())
                    .slotId(request.slotId())
                    .typeId(engine.getTypeId())
                    .upgradeLevel(engine.getUpgradeLevel())
                    .cost(engine.getCost())
                    .name(engine.getName())
                    .dsc(engine.getDsc())
                    .speed(engine.getSpeed())
                    .jump(engine.getJump())
                    .equipmentTypeId(engine.getEquipmentTypeId())
                    .build();

    public static final Function<FuelTankMessage, Item> fuelTankMessageToItem = message -> FuelTank.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .typeId(message.getTypeId())
            .upgradeLevel(message.getUpgradeLevel())
            .cost(message.getCost())
            .name(message.getName())
            .dsc(message.getDsc())
            .capacity(message.getCapacity())
            .equipmentTypeId(message.getEquipmentTypeId())
            .build();

    public static final BiFunction<FuelTank, ItemUpdate, FuelTank> tankToNewTank =
            (fuelTank, request) -> FuelTank.builder()
                    .id(fuelTank.getId())
                    .slotId(request.slotId())
                    .typeId(fuelTank.getTypeId())
                    .upgradeLevel(fuelTank.getUpgradeLevel())
                    .cost(fuelTank.getCost())
                    .name(fuelTank.getName())
                    .dsc(fuelTank.getDsc())
                    .capacity(fuelTank.getCapacity())
                    .equipmentTypeId(fuelTank.getEquipmentTypeId())
                    .build();

    public static final Function<CargoHookMessage, Item> cargoHookMessageToItem = message -> CargoHook.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .typeId(message.getTypeId())
            .upgradeLevel(message.getUpgradeLevel())
            .cost(message.getCost())
            .name(message.getName())
            .dsc(message.getDsc())
            .loadCapacity(message.getLoadCapacity())
            .radius(message.getRadius())
            .equipmentTypeId(message.getEquipmentTypeId())
            .build();

    public static final BiFunction<CargoHook, ItemUpdate, CargoHook> cargoHookToNewHook =
            (cargoHook, request) -> CargoHook.builder()
                    .id(cargoHook.getId())
                    .slotId(request.slotId())
                    .typeId(cargoHook.getTypeId())
                    .upgradeLevel(cargoHook.getUpgradeLevel())
                    .cost(cargoHook.getCost())
                    .name(cargoHook.getName())
                    .dsc(cargoHook.getDsc())
                    .loadCapacity(cargoHook.getLoadCapacity())
                    .radius(cargoHook.getRadius())
                    .equipmentTypeId(cargoHook.getEquipmentTypeId())
                    .build();

    public static final Function<HullMessage, Item> hullMessageToItem = message -> Hull.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .typeId(message.getTypeId())
            .upgradeLevel(message.getUpgradeLevel())
            .cost(message.getCost())
            .name(message.getName())
            .dsc(message.getDsc())
            .hp(message.getHp())
            .evasion(message.getEvasion())
            .armor(message.getArmor())
            .equipmentTypeId(message.getEquipmentTypeId())
            .config(message.getConfig())
            .build();

    public static final BiFunction<Hull, ItemUpdate, Hull> hullToNewHull =
            (hull, request) -> Hull.builder()
                    .id(hull.getId())
                    .slotId(request.slotId())
                    .typeId(hull.getTypeId())
                    .upgradeLevel(hull.getUpgradeLevel())
                    .cost(hull.getCost())
                    .name(hull.getName())
                    .dsc(hull.getDsc())
                    .hp(hull.getHp())
                    .evasion(hull.getEvasion())
                    .armor(hull.getArmor())
                    .equipmentTypeId(hull.getEquipmentTypeId())
                    .config(hull.getConfig())
                    .build();
}
