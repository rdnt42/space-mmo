package marowak.dev.service.item;

import marowak.dev.dto.item.Item;
import marowak.dev.request.CharacterInventoryItemRequest;
import marowak.dev.response.player.CharacterInventoryResponse;
import message.ItemMessage;

public interface InventoryService {
    CharacterInventoryResponse getInventory(String playerName);

    void updateInventoryFromStorage(ItemMessage message);

    Item updateInventory(CharacterInventoryItemRequest request, String playerName);
}
