package marowak.dev.service.mapper;

import marowak.dev.entity.Item;
import message.ItemMessage;

import java.util.function.BiFunction;

public class BuilderHelper {
    private BuilderHelper() {
    }

    public static final BiFunction<Item, ItemMessage.Builder, ItemMessage.Builder> itemToBuilder =
            (item, baseBuilder) -> baseBuilder
                    .id(item.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(Integer.valueOf(item.upgradeLevel()))
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .x(item.x())
                    .y(item.y());
}
