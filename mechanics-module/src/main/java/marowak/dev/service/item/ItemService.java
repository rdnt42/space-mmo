package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.dto.item.Item;
import marowak.dev.enums.ItemTypes;
import marowak.dev.request.ItemUpdate;
import message.ItemMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName);

    Mono<Void> updateInventoryFromStorage(ItemMessage message);

    Mono<ItemUpdate> updateInventoryFromClient(ItemUpdate request, String playerName);

    Flux<Item> getItems(String playerName);

    Mono<Item> getItem(String characterName, ItemTypes type);
}
