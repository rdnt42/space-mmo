package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import message.ItemMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

public interface CharacterItemService {
    Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName);

    Mono<Void> addItem(ItemMessage message);

    Mono<ItemUpdate> updateItem(ItemUpdate request, String playerName);

    Mono<ItemView> getItem(String characterName, long itemId);

    Mono<InventoryView> getInventory(String playerName);

}
