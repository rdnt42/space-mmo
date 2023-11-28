package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import marowak.dev.dto.item.ItemDto;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

public interface CharacterItemService {
    Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName);

    Mono<Void> addItem(ItemDto dto);

    Mono<ItemUpdate> updateItem(ItemUpdate request, String playerName);

    Mono<InventoryView> getInventory(String playerName);

}
