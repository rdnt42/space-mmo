package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.item.Item;
import marowak.dev.enums.StorageType;
import marowak.dev.service.broker.ItemClient;
import marowak.dev.service.character.CharacterShipService;
import message.*;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final ItemClient itemClient;
    private final CharacterShipService characterShipService;
    private final SpaceItemService spaceItemService;

    @Override
    public Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(key)
                .characterName(characterName)
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send getting Items init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send getting Items init, key: {}, character: {}", key, characterName));
    }

    @Override
    public Mono<Void> addItemFromStorage(ItemMessage message) {
        Item item;
        switch (message) {
            case EngineMessage engine -> item = BuilderHelper.engineMessageToItem.apply(engine);
            case FuelTankMessage fuelTank -> item = BuilderHelper.fuelTankMessageToItem.apply(fuelTank);
            case CargoHookMessage cargoHook -> item = BuilderHelper.cargoHookMessageToItem.apply(cargoHook);
            case HullMessage hull -> item = BuilderHelper.hullMessageToItem.apply(hull);
            case WeaponMessage weapon -> item = BuilderHelper.weaponMessageToItem.apply(weapon);
            default -> throw new IllegalStateException("Unknown Item message, key: " + message.getKey());
        }
        StorageType storageType = StorageType.from(item.getStorageId());

        if (storageType.isShipStorage()) {
            return characterShipService.addItem(message.getCharacterName(), item)
                    .then();
        } else if (storageType.isSpaceStorage()) {
            return spaceItemService.addItem(item);
        }

        return Mono.empty();
    }

    @Override
    public Mono<ItemUpdate> updateItemFromClient(ItemUpdate request, String characterName) {
        return characterShipService.updateItem(characterName, request)
                .flatMap(item -> sendItemUpdate(item.getView(), characterName)
                        .doOnNext(c -> log.info("Inventory updated from client id: {}, slot: {}", item.getId(), item.getSlotId()))
                        .then(Mono.just(ItemUpdate.builder()
                                .id(item.getId())
                                .slotId(item.getSlotId())
                                .storageId(item.getStorageId())
                                .build())));
    }

    @Override
    public Mono<InventoryView> getInventoryItems(String characterName) {
        return characterShipService.getInventoryInfo(characterName);
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

}
