package marowak.dev.service.equipment;

import marowak.dev.response.player.CharacterInventoryResponse;
import message.EquipmentMessage;

public interface CharacterInventoryService {
    CharacterInventoryResponse getInventory(String playerName);

    void updateInventory(EquipmentMessage message);
}
