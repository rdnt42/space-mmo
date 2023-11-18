package marowak.dev.service.mapper;

import jakarta.inject.Singleton;
import marowak.dev.entity.FuelTank;
import marowak.dev.entity.Item;
import marowak.dev.service.item.mark.FuelTankItemService;
import message.FuelTankMessage;
import message.ItemMessage;

import java.util.function.BiFunction;

@Singleton
public class FuelTankItemMapper implements ItemMapper<FuelTank>, FuelTankItemService {

    @Override
    public ItemMessage map(Item item, FuelTank extension) {
        return fuelTankToMessage.apply(extension, item);
    }

    public static final BiFunction<FuelTank, Item, ItemMessage> fuelTankToMessage =
            (fuelTank, item) -> ((FuelTankMessage.FuelTankMessageBuilder<?, ?>)
                    BuilderHelper.itemToBuilder.apply(item, FuelTankMessage.builder()))
                    .capacity(fuelTank.capacity())
                    .equipmentTypeId(fuelTank.fuelTankTypeId())
                    .build();

}
