package marowak.dev.service.mapper;

import jakarta.inject.Singleton;
import marowak.dev.entity.Engine;
import marowak.dev.entity.Item;
import marowak.dev.service.item.mark.EngineItemService;
import message.EngineMessage;
import message.ItemMessage;

import java.util.function.BiFunction;

@Singleton
public class EngineItemMapper implements ItemMapper<Engine>, EngineItemService {

    @Override
    public ItemMessage map(Item item, Engine extension) {
        return engineToMessage.apply(extension, item);
    }

    public static final BiFunction<Engine, Item, ItemMessage> engineToMessage =
            (engine, item) -> EngineMessage.builder()
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

}
