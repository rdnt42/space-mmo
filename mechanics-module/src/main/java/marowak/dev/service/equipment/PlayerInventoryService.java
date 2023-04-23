package marowak.dev.service.equipment;

import marowak.dev.response.player.PlayerInventoryResponse;

public interface PlayerInventoryService {
    PlayerInventoryResponse getPlayerInventory(String playerName);
}
