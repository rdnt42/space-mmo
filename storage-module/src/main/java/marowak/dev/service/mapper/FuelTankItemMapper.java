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
            (fuelTank, item) -> FuelTankMessage.builder()
                    .id(fuelTank.id())
                    .slotId(item.slotId())
                    .storageId(item.storageId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .capacity(fuelTank.capacity())
                    .equipmentTypeId(fuelTank.fuelTankTypeId())
                    .build();

}
