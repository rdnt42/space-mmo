package marowak.dev.service.item;

import marowak.dev.response.player.CharacterInventoryResponse;
import message.ItemMessage;

public interface CharacterInventoryService {
    CharacterInventoryResponse getInventory(String playerName);

    void updateInventory(ItemMessage message);
}
