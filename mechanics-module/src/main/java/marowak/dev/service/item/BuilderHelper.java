package marowak.dev.service.item;

import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.FuelTank;
import marowak.dev.dto.item.Item;
import marowak.dev.request.CharacterInventoryItemRequest;
import message.EngineMessage;
import message.FuelTankMessage;

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

    public static final BiFunction<Engine, CharacterInventoryItemRequest, Engine> engineToNewEngine =
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

    public static final BiFunction<FuelTank, CharacterInventoryItemRequest, FuelTank> tankToNewTank =
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
}
