package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.player.CharacterInventoryResponse;
import message.ItemMessage;

public interface ItemService {
    void sendGetItems(ItemMessageKey key, String characterName);

    CharacterInventoryResponse getInventory(String playerName);

    void updateInventoryFromStorage(ItemMessage message);

    ItemUpdate updateInventoryFromClient(ItemUpdate request, String playerName);
}
