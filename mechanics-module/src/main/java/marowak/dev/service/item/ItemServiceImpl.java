package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.item.CargoHook;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.FuelTank;
import marowak.dev.dto.item.Item;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.player.CharacterInventoryResponse;
import marowak.dev.service.broker.ItemClient;
import message.CargoHookMessage;
import message.EngineMessage;
import message.FuelTankMessage;
import message.ItemMessage;

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
                .slots(characterInventory.slots())
                .items(characterInventory.items().values())
                .build();
    }

    @Override
    public void updateInventoryFromStorage(ItemMessage message) {
        playerInventoryMap.putIfAbsent(message.getCharacterName(), createInventory());
        CharacterInventory inventory = playerInventoryMap.get(message.getCharacterName());

        Item item;
        // TODO maybe polymorphism
        if (message instanceof EngineMessage engine) {
            item = BuilderHelper.engineMessageToItem.apply(engine);
        } else if (message instanceof FuelTankMessage fuelTank) {
            item = BuilderHelper.fuelTankMessageToItem.apply(fuelTank);
        } else if (message instanceof CargoHookMessage cargoHook) {
            item = BuilderHelper.cargoHookMessageToItem.apply(cargoHook);

        } else {
            throw new IllegalArgumentException("Unknown Item message, key: " + message.getKey());
        }

        inventory.items().put(item.getId(), item);
        log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());
    }

    @Override
    public ItemUpdate updateInventoryFromClient(ItemUpdate request, String playerName) {
        CharacterInventory inventory = Optional.ofNullable(playerInventoryMap.get(playerName))
                .orElseThrow();
        Item item = Optional.ofNullable(inventory.items().get(request.id()))
                .orElseThrow();

        Item newItem;
        if (item instanceof Engine engine) {
            newItem = BuilderHelper.engineToNewEngine.apply(engine, request);
        } else if (item instanceof FuelTank fuelTank) {
            newItem = BuilderHelper.tankToNewTank.apply(fuelTank, request);
        } else if (item instanceof CargoHook cargoHook) {
            newItem = BuilderHelper.cargoHookToNewHook.apply(cargoHook, request);
        } else {
            throw new UnsupportedOperationException("Cannot convert item with id: " + item.getId() + ", and type: " + item.getTypeId());
        }

        playerInventoryMap.get(playerName)
                .items()
                .put(newItem.getId(), newItem);
        sendItemUpdate(newItem);
        log.info("updateInventory id: {}, slot: {}", request.id(), request.slotId());

        return ItemUpdate.builder()
                .id(newItem.getId())
                .slotId(newItem.getSlotId())
                .build();
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
    }

}
