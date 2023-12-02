package marowak.dev.service.item;

import marowak.dev.api.request.ItemUpdateRequest;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import message.ItemMessage;
import reactor.core.publisher.Mono;

public interface CharacterItemService {
    Mono<Void> addItem(ItemMessage message);

    Mono<ItemView> addItemFromSpace(long itemId, String characterName);

    Mono<ItemUpdateRequest> updateItem(ItemUpdateRequest request, String characterName);

    Mono<InventoryView> getInventory(String characterName);

}
