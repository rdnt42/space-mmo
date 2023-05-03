package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.item.Engine;
import marowak.dev.dto.item.Item;
import marowak.dev.response.player.CharacterInventoryResponse;
import message.EngineMessage;
import message.ItemMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Singleton
public class CharacterInventoryServiceImpl implements CharacterInventoryService {
    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();

    private static final Set<Integer> baseConfig = new HashSet<>();

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
    public void updateInventory(ItemMessage message) {
        playerInventoryMap.putIfAbsent(message.getCharacterName(), createInventory());
        CharacterInventory inventory = playerInventoryMap.get(message.getCharacterName());

        if (message instanceof EngineMessage engine) {
            Item item = engineMessageToItem.apply(engine);
            inventory.items().put(item.getId(), item);
            log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());
        }
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

    static {
        baseConfig.add(1); // engine
        baseConfig.add(2); // fuel tank
        baseConfig.add(3); // scanner
        baseConfig.add(4); // radar
    }
}
