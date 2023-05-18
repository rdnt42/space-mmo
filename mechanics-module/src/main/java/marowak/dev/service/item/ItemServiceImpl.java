package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.item.*;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.character.CharacterInventoryResponse;
import marowak.dev.service.broker.ItemClient;
import message.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {

    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();
    private static final Set<Integer> baseConfig = new HashSet<>();
    private final ItemClient itemClient;

    @Override
    public void sendGetItems(ItemMessageKey key, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(key)
                .build();

        itemClient.sendItems(message)
                .doOnError(e -> log.error("Send getting Items init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send getting Items init, key: {}, character: {}", key, characterName))
                .subscribe();
    }

    @Override
    public CharacterInventoryResponse getInventory(String playerName) {
        CharacterInventory characterInventory = playerInventoryMap.get(playerName);
        if (characterInventory == null) {
            return null;
        }

        return CharacterInventoryResponse.builder()
                .items(characterInventory.items().values())
                .build();
    }

    @Override
    public void updateInventoryFromStorage(ItemMessage message) {
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
    public Flux<Item> getItems(String playerName) {
        return Flux.fromStream(playerInventoryMap.get(playerName)
                .items().values().stream());
    }

    // TODO npe
    @Override
    public Mono<Item> getItem(String characterName, ItemTypes type) {
        return Mono.justOrEmpty(playerInventoryMap.get(characterName)
                .items().values().stream()
                .filter(item -> item.getTypeId() == type.getTypeId())
                .findFirst());
    }


    private CharacterInventory createInventory() {
        return CharacterInventory.builder()
                .slots(baseConfig)
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


    // TODO #62
    static {
        baseConfig.add(1); // engine
        baseConfig.add(2); // fuel tank
        baseConfig.add(3); // scanner
        baseConfig.add(4); // radar
        baseConfig.add(6); // cargo hook
        baseConfig.add(8); // hull
    }

}
