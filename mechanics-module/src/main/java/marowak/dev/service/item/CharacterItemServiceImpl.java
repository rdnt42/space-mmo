package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.character.CargoItem;
import marowak.dev.character.Item;
import marowak.dev.dto.item.*;
import marowak.dev.enums.StorageType;
import marowak.dev.service.character.CharacterShipService;
import message.ItemMessage;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterItemServiceImpl implements CharacterItemService {
    private final CharacterShipService characterShipService;
    private final ItemStorage itemStorage;

    @Override
    public Mono<Void> addItem(ItemMessage message) {
        return itemStorage.addItem(message)
                .flatMap(dto -> {
                    Item item = map(dto);
                    return characterShipService.getShip(dto.getCharacterName())
                            .flatMap(ship -> {
                                ship.addItem(item);
                                return Mono.empty();
                            });
                });
    }

    @Override
    public Mono<ItemUpdate> updateItem(ItemUpdate request, String characterName) {
        return itemStorage.getItem(request.id())
                .flatMap(dto -> {
                    dto.setSlotId(request.slotId());
                    dto.setStorageId(request.storageId());
                    Item item = map(dto);

                    return characterShipService.getShip(characterName)
                            .map(ship -> ship.updateItem(item))
                            .then(itemStorage.updateItem(dto))
                            .then(Mono.just(new ItemUpdate(dto.getId(), dto.getSlotId(), dto.getStorageId())))
                            .doOnSuccess(u -> log.info("Inventory updated from client id: {}, slot: {}", u.id(), u.slotId()));
                });
    }

    @Override
    public Mono<ItemView> getItem(String characterName, long itemId) {
        return characterShipService.getItem(characterName, itemId);
    }

    @Override
    public Mono<InventoryView> getInventory(String characterName) {
        // TODO items

        return characterShipService.getInventory(characterName);
    }

    private Mono<Void> sendItemUpdate(ItemView item, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(ItemMessageKey.ITEM_UPDATE)
                .id(item.getId())
                .characterName(characterName)
                .slotId(item.getSlotId())
                .storageId(item.getStorageId())
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send Items init error, key{}, character: {}, error: {}",
                        message.getKey(), message.getCharacterName(), e.getMessage()))
                .then();
    }

    private Item map(ItemDto dto) {
        Item item;
        if (StorageType.STORAGE_TYPE_HULL.equals(dto.getStorageId())) {
            switch (dto) {
                case EngineDto engine -> item = BuilderHelper.dtoToEngine.apply(engine);
                case FuelTankDto fuelTank -> item = BuilderHelper.dtoToFuelTank.apply(fuelTank);
                case CargoHookDto cargoHook -> item = BuilderHelper.dtoToCargoHook.apply(cargoHook);
                case HullDto hull -> item = BuilderHelper.dtoToHull.apply(hull);
                case WeaponDto weapon -> item = BuilderHelper.dtoToWeapon.apply(weapon);
                default -> throw new IllegalStateException("Unknown Item type, key: " + dto.getTypeId());
            }
        } else if (StorageType.STORAGE_TYPE_HOLD.equals(dto.getStorageId())) {
            item = CargoItem.builder()
                    .id(dto.getId())
                    .slotId(dto.getSlotId())
                    .build();
        } else {
            throw new IllegalStateException("Unsupported storage id: " + dto.getStorageId());
        }

        return item;
    }

}
