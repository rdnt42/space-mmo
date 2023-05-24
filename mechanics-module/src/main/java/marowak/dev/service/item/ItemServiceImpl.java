package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.InventoryInfo;
import marowak.dev.dto.item.*;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.ItemUpdate;
import marowak.dev.service.broker.ItemClient;
import message.*;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {

    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();
    private final ItemClient itemClient;

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
        playerInventoryMap.putIfAbsent(message.getCharacterName(), createInventory());
        CharacterInventory inventory = playerInventoryMap.get(message.getCharacterName());

        Item item;
        switch (message) {
            case EngineMessage engine -> item = BuilderHelper.engineMessageToItem.apply(engine);
            case FuelTankMessage fuelTank -> item = BuilderHelper.fuelTankMessageToItem.apply(fuelTank);
            case CargoHookMessage cargoHook -> item = BuilderHelper.cargoHookMessageToItem.apply(cargoHook);
            case HullMessage hull -> item = BuilderHelper.hullMessageToItem.apply(hull);
            default -> throw new IllegalStateException("Unknown Item message, key: " + message.getKey());
        }

        inventory.items().put(item.getId(), item);
        log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());

        return Mono.empty();
    }

    @Override
    public Mono<ItemUpdate> updateInventoryFromClient(ItemUpdate request, String playerName) {
        CharacterInventory inventory = Optional.ofNullable(playerInventoryMap.get(playerName))
                .orElseThrow();
        Item item = Optional.ofNullable(inventory.items().get(request.id()))
                .orElseThrow();

        Item newItem = switch (item) {
            case Engine engine -> BuilderHelper.engineToNewEngine.apply(engine, request);
            case FuelTank fuelTank -> BuilderHelper.tankToNewTank.apply(fuelTank, request);
            case CargoHook cargoHook -> BuilderHelper.cargoHookToNewHook.apply(cargoHook, request);
            case Hull hull -> BuilderHelper.hullToNewHull.apply(hull, request);
            case null, default ->
                    throw new UnsupportedOperationException("Cannot convert item with id: " + item.getId() + ", and type: " + item.getTypeId());
        };

        playerInventoryMap.get(playerName)
                .items()
                .put(newItem.getId(), newItem);
        sendItemUpdate(newItem);
        log.info("updateInventory id: {}, slot: {}", request.id(), request.slotId());

        return Mono.just(ItemUpdate.builder()
                .id(newItem.getId())
                .slotId(newItem.getSlotId())
                .build());
    }

    @Override
    public Mono<InventoryInfo> getItems(String playerName) {
        CharacterInventory inventory = playerInventoryMap.get(playerName);
        if (inventory == null) {
            return Mono.empty();
        }

        // TODO rework when using storageId
        Hull hull = (Hull) inventory.items().values().stream()
                .filter(i -> i.getTypeId() == ItemTypes.ITEM_TYPE_HULL.getTypeId() && i.getSlotId() == null)
                .findFirst()
                .orElse(null);

        return Mono.just(InventoryInfo.builder()
                .items(inventory.items().values())
                .config(hull == null ? 0 : hull.getConfig())
                .build());
    }

    @Override
    public Mono<Item> getItem(String characterName, ItemTypes type) {
        return Mono.justOrEmpty(playerInventoryMap.get(characterName))
                .flatMap(playerInventory -> Mono.justOrEmpty(playerInventory.items().values().stream()
                        .filter(item -> item.getTypeId() == type.getTypeId())
                        .findFirst()))
                .switchIfEmpty(Mono.empty());
    }


    private CharacterInventory createInventory() {
        return CharacterInventory.builder()
                .items(new HashMap<>())
                .build();
    }

    private void sendItemUpdate(Item item) {
        ItemMessage message = ItemMessage.builder()
                .key(ItemMessageKey.ITEMS_UPDATE)
                .id(item.getId())
                .slotId(item.getSlotId())
                .build();

        itemClient.sendItems(message)
                .doOnError(e -> log.error("Send Items init error, key{}, character: {}, error: {}",
                        message.getKey(), message.getCharacterName(), e.getMessage()))
                .subscribe();
    }

}
