package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.entity.CargoHook;
import marowak.dev.entity.Engine;
import marowak.dev.entity.FuelTank;
import marowak.dev.entity.Item;
import marowak.dev.service.TriFunction;
import message.CargoHookMessage;
import message.EngineMessage;
import message.FuelTankMessage;
import message.ItemMessage;

public class BuilderHelper {
    public static final TriFunction<Engine, Item, ItemMessageKey, ItemMessage> engineToMessage =
            (engine, item, key) -> EngineMessage.builder()
                    .key(key)
                    .id(engine.id())
                    .slotId(item.slotId())
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
}
