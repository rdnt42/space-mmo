package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.item.Hull;
import marowak.dev.dto.item.Item;
import marowak.dev.enums.ItemType;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.InventoryInfo;
import marowak.dev.service.broker.ItemClient;
import marowak.dev.service.character.CharacterShipService;
import message.*;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {

    // TODO create settings
    private static final int HULL_SLOT_ID = 8;
    public static final int HULL_STORAGE_ID = 1;
    public static final int HOLD_STORAGE_ID = 2;

    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();
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
//    @Override
//    public Mono<Void> updateInventoryFromStorage(ItemMessage message) {
//        playerInventoryMap.putIfAbsent(message.getCharacterName(), createInventory());
//        CharacterInventory inventory = playerInventoryMap.get(message.getCharacterName());
//
//        Item item;
//        switch (message) {
//            case EngineMessage engine -> item = BuilderHelper.engineMessageToItem.apply(engine);
//            case FuelTankMessage fuelTank -> item = BuilderHelper.fuelTankMessageToItem.apply(fuelTank);
//            case CargoHookMessage cargoHook -> item = BuilderHelper.cargoHookMessageToItem.apply(cargoHook);
//            case HullMessage hull -> item = BuilderHelper.hullMessageToItem.apply(hull);
//            case WeaponMessage weapon -> item = BuilderHelper.weaponMessageToItem.apply(weapon);
//            default -> throw new IllegalStateException("Unknown Item message, key: " + message.getKey());
//        }
//
//        inventory.items().put(item.getId(), item);
//        item.init();
//        log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());
//
//        return Mono.empty();
//    }

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
//    @Override
//    public Mono<ItemUpdate> updateInventoryFromClient(ItemUpdate request, String playerName) {
//        CharacterInventory inventory = Optional.ofNullable(playerInventoryMap.get(playerName))
//                .orElseThrow();
//        Item item = Optional.ofNullable(inventory.items().get(request.id()))
//                .orElseThrow();
//
//        item.updatePosition(request.slotId(), request.storageId());
//        sendItemUpdate(item);
//        log.info("updateInventory id: {}, slot: {}", request.id(), request.slotId());
//
//        return Mono.just(ItemUpdate.builder()
//                .id(item.getId())
//                .slotId(item.getSlotId())
//                .storageId(item.getStorageId())
//                .build());
//    }

    @Override
    public Mono<InventoryInfo> getInventoryItems(String playerName) {
        CharacterInventory inventory = playerInventoryMap.get(playerName);
        if (inventory == null) {
            return Mono.empty();
        }

        Hull hull = (Hull) inventory.items().values().stream()
                .filter(this::equippedHull)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Hull cannot be null, playerName: " + playerName));

        List<Item> inventoryItems = inventory.items().values()
                .stream()
                .filter(this::isInventoryItem).toList();

        return Mono.just(InventoryInfo.builder()
                .items(inventoryItems)
                .config(hull == null ? 0 : hull.getConfig())
                .build());
    }

//    @Override
//    public Mono<InventoryInfo> getInventoryItems(String playerName) {
//        CharacterInventory inventory = playerInventoryMap.get(playerName);
//        if (inventory == null) {
//            return Mono.empty();
//        }
//
//        Hull hull = (Hull) inventory.items().values().stream()
//                .filter(this::equippedHull)
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("Hull cannot be null, playerName: " + playerName));
//
//        List<Item> inventoryItems = inventory.items().values()
//                .stream()
//                .filter(this::isInventoryItem).toList();
//
//        return Mono.just(InventoryInfo.builder()
//                .items(inventoryItems)
//                .config(hull == null ? 0 : hull.getConfig())
//                .build());
//    }

    private boolean isInventoryItem(Item item) {
        return item.getStorageId() == HULL_STORAGE_ID || item.getStorageId() == HOLD_STORAGE_ID;
    }

    private boolean equippedHull(Item item) {
        return item.getTypeId() == ItemType.ITEM_TYPE_HULL.getTypeId() &&
                item.getStorageId() == HULL_STORAGE_ID &&
                item.getSlotId() == HULL_SLOT_ID;
    }

    @Override
    public Mono<Item> getFirstEquippedItem(String characterName, ItemType type) {
        return Mono.from(getEquippedItems(characterName, type));
    }

    @Override
    public Flux<Item> getEquippedItems(String characterName, ItemType type) {
        return Flux.fromStream(playerInventoryMap.get(characterName)
                .items().values().stream()
                .filter(item -> item.getStorageId() == HULL_STORAGE_ID && item.getTypeId() == type.getTypeId()));
    }

    @Override
    public Flux<Item> getEquippedItems(ItemType type) {
        return Flux.fromStream(playerInventoryMap.values().stream()
                .flatMap(inventory -> inventory.items().values().stream())
                .filter(item -> item.getStorageId() == HULL_STORAGE_ID && item.getTypeId() == type.getTypeId()));
    }


    private CharacterInventory createInventory() {
        return CharacterInventory.builder()
                .items(new HashMap<>())
                .build();
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
