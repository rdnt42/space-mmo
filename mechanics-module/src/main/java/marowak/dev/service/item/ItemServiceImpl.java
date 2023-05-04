package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Item;
import marowak.dev.request.CharacterInventoryItemRequest;
import marowak.dev.response.player.CharacterInventoryResponse;
import marowak.dev.service.broker.ItemClient;
import message.EngineMessage;
import message.ItemMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {

    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();
    private static final Set<Integer> baseConfig = new HashSet<>();
    private final ItemClient itemClient;

    @Override
    public void sendGetItems(ItemMessageKey key, String characterName) {
        // TODO add other items type
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

        if (message instanceof EngineMessage engine) {
            Item item = engineMessageToItem.apply(engine);
            inventory.items().put(item.getId(), item);
            log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());
        }
    }

    @Override
    public Item updateInventoryFromClient(CharacterInventoryItemRequest request, String playerName) {
        CharacterInventory inventory = Optional.ofNullable(playerInventoryMap.get(playerName))
                .orElseThrow();
        Item item = Optional.ofNullable(inventory.items().get(request.itemId()))
                .orElseThrow();
        if (item instanceof Engine engine) {
            Item newItem = Engine.builder()
                    .id(item.getId())
                    .slotId(request.slotId())
                    .itemTypeId(item.getItemTypeId())
                    .upgradeLevel(item.getUpgradeLevel())
                    .cost(item.getCost())
                    .name(item.getName())
                    .dsc(item.getDsc())
                    .speed(engine.getSpeed())
                    .jump(engine.getJump())
                    .subTypeId(engine.getSubTypeId())
                    .build();

            playerInventoryMap.get(playerName)
                    .items()
                    .put(newItem.getId(), newItem);

            sendItemUpdate(newItem);
            log.info("updateInventory id: {}, slot: {}", request.itemId(), request.slotId());
            return newItem;
        }

        return null;
    }

    private CharacterInventory createInventory() {
        return CharacterInventory.builder()
                .slots(baseConfig)
                .items(new HashMap<>())
                .build();
    }

    private final Function<EngineMessage, Item> engineMessageToItem = message -> Engine.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .equipped(message.isEquipped())
            .itemTypeId(message.getItemTypeId())
            .upgradeLevel(message.getUpgradeLevel())
            .cost(message.getCost())
            .name(message.getName())
            .dsc(message.getDsc())
            .speed(message.getSpeed())
            .jump(message.getJump())
            .subTypeId(message.getEngineType())
            .build();


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
    }

}
