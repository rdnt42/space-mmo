package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.CharacterInventory;
import marowak.dev.dto.item.Cargo;
import marowak.dev.dto.item.Item;
import marowak.dev.response.player.CharacterInventoryResponse;
import message.ItemMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Singleton
public class CharacterInventoryServiceImpl implements CharacterInventoryService {
    private final Map<String, CharacterInventory> playerInventoryMap = new ConcurrentHashMap<>();

    @Override
    public CharacterInventoryResponse getInventory(String playerName) {
        CharacterInventory characterInventory = playerInventoryMap.get(playerName);
        if (characterInventory == null) {
            return null;
        }

        return CharacterInventoryResponse.builder()
                .slots(characterInventory.slots())
                .cargos(characterInventory.cargos().values())
                .items(characterInventory.items().values())
                .build();
    }

    @Override
    public void updateInventory(ItemMessage message) {
        playerInventoryMap.putIfAbsent(message.getCharacterName(), createInventory());
        CharacterInventory inventory = playerInventoryMap.get(message.getCharacterName());

        Item item = messageToItem.apply(message);
        inventory.items().put(item.getId(), item);
        if (!item.isEquipped()) {
            Cargo cargo = new Cargo(1, 1);
            inventory.cargos().put(cargo.id(), cargo);
        }
        inventory.slots().add(item.getSlotId());

        log.info("Inventory update successful, character name: {}, item id: {}", message.getCharacterName(), item.getId());
    }

    private CharacterInventory createInventory() {
        return CharacterInventory.builder()
                .slots(new HashSet<>())
                .cargos(new HashMap<>())
                .items(new HashMap<>())
                .build();
    }

    private final Function<ItemMessage, Item> messageToItem = message -> Item.builder()
            .id(message.getId())
            .slotId(message.getSlotId())
            .equipped(message.isEquipped())
            .itemTypeId(message.getItemTypeId())
            .upgradeLevel(message.getUpgradeLevel())
            .cost(message.getCost())
            .build();
}
