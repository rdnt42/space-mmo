package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.dto.item.Item;
import marowak.dev.request.CharacterInventoryItemRequest;
import marowak.dev.response.player.CharacterInventoryResponse;
import message.ItemMessage;

public interface ItemService {
    void sendGetItems(ItemMessageKey key, String characterName);

    CharacterInventoryResponse getInventory(String playerName);

    void updateInventoryFromStorage(ItemMessage message);

    Item updateInventoryFromClient(CharacterInventoryItemRequest request, String playerName);
}
