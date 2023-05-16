package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.dto.item.Item;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.ItemUpdate;
import marowak.dev.response.character.CharacterInventoryResponse;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    void sendGetItems(ItemMessageKey key, String characterName);

    CharacterInventoryResponse getInventory(String playerName);

    void updateInventoryFromStorage(ItemMessage message);

    ItemUpdate updateInventoryFromClient(ItemUpdate request, String playerName);

    <T extends Item> Flux<T> getItems(String playerName);

    // TODO npe
    Mono<Item> getItem(String characterName, ItemTypes type);
}
