package marowak.dev.service.item;

import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import message.ItemMessage;
import reactor.core.publisher.Mono;

public interface CharacterItemService {
    Mono<Void> addItem(ItemMessage message);

    Mono<ItemUpdate> updateItem(ItemUpdate request, String characterName);

    Mono<InventoryView> getInventory(String characterName);

}
