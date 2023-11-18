package marowak.dev.service.mapper;

import jakarta.inject.Singleton;
import marowak.dev.entity.CargoHook;
import marowak.dev.entity.Item;
import marowak.dev.service.item.mark.CargoHookItemService;
import message.CargoHookMessage;
import message.ItemMessage;

import java.util.function.BiFunction;

@Singleton
public class CargoHookItemMapper implements ItemMapper<CargoHook>, CargoHookItemService {

    @Override
    public ItemMessage map(Item item, CargoHook extension) {
        return cargoHookToMessage.apply(extension, item);
    }

    public static final BiFunction<CargoHook, Item, ItemMessage> cargoHookToMessage =
            (cargoHook, item) -> ((CargoHookMessage.CargoHookMessageBuilder<?, ?>)
                    BuilderHelper.itemToBuilder.apply(item, CargoHookMessage.builder()))
                    .loadCapacity(cargoHook.loadCapacity())
                    .radius(cargoHook.radius())
                    .equipmentTypeId(cargoHook.cargoHookTypeId())
                    .build();

}
