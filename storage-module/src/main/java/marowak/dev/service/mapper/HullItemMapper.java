package marowak.dev.service.mapper;

import jakarta.inject.Singleton;
import marowak.dev.entity.Hull;
import marowak.dev.entity.Item;
import marowak.dev.service.item.mark.HullItemService;
import message.HullMessage;
import message.ItemMessage;

import java.util.function.BiFunction;

@Singleton
public class HullItemMapper implements ItemMapper<Hull>, HullItemService {

    @Override
    public ItemMessage map(Item item, Hull extension) {
        return hullToMessage.apply(extension, item);
    }

    public static final BiFunction<Hull, Item, ItemMessage> hullToMessage =
            (hull, item) -> ((HullMessage.Builder)
                    BuilderHelper.itemToBuilder.apply(item, HullMessage.builder()))
                    .hp(hull.hp())
                    .evasion(hull.evasion())
                    .armor(hull.armor())
                    .equipmentTypeId(hull.hullTypeId())
                    .config(hull.config())
                    .build();

}
