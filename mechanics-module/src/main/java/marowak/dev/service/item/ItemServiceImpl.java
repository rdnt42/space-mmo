package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.Item;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.InventoryInfo;
import marowak.dev.service.broker.ItemClient;
import marowak.dev.service.character.CharacterShipService;
import message.*;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {

    // TODO create settings
    public static final int HULL_STORAGE_ID = 1;
    public static final int HOLD_STORAGE_ID = 2;

    private final ItemClient itemClient;
    private final CharacterShipService characterShipService;

    @Override
    public Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(key)
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send getting Items init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send getting Items init, key: {}, character: {}", key, characterName));
    }

    @Override
    public Mono<Void> updateInventoryFromStorage(ItemMessage message) {
        Item item;
        switch (message) {
            case EngineMessage engine -> item = BuilderHelper.engineMessageToItem.apply(engine);
            case FuelTankMessage fuelTank -> item = BuilderHelper.fuelTankMessageToItem.apply(fuelTank);
            case CargoHookMessage cargoHook -> item = BuilderHelper.cargoHookMessageToItem.apply(cargoHook);
            case HullMessage hull -> item = BuilderHelper.hullMessageToItem.apply(hull);
            case WeaponMessage weapon -> item = BuilderHelper.weaponMessageToItem.apply(weapon);
            default -> throw new IllegalStateException("Unknown Item message, key: " + message.getKey());
        }
        log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());

        return characterShipService.addItem(message.getCharacterName(), item);
    }

    @Override
    public Mono<ItemUpdate> updateInventoryFromClient(ItemUpdate request, String playerName) {
        log.info("updateInventory id: {}, slot: {}", request.id(), request.slotId());

        return characterShipService.updateItem(playerName, request)
                .flatMap(item -> sendItemUpdate(item)
                        .then(Mono.just(ItemUpdate.builder()
                                .id(item.getId())
                                .slotId(item.getSlotId())
                                .storageId(item.getStorageId())
                                .build())));
    }

    @Override
    public Mono<InventoryInfo> getInventoryItems(String characterName) {
        return characterShipService.getInventoryInfo(characterName);
    }


    private Mono<Void> sendItemUpdate(Item item) {
        ItemMessage message = ItemMessage.builder()
                .key(ItemMessageKey.ITEMS_UPDATE)
                .id(item.getId())
                .slotId(item.getSlotId())
                .storageId(item.getStorageId())
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send Items init error, key{}, character: {}, error: {}",
                        message.getKey(), message.getCharacterName(), e.getMessage()))
                .then(Mono.empty());
    }

}
