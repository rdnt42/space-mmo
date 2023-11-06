package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import message.ItemMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

public interface ItemService {
    Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName);

    Mono<Void> updateInventoryFromStorage(ItemMessage message);

    Mono<ItemUpdate> updateInventoryFromClient(ItemUpdate request, String playerName);

    Mono<InventoryView> getInventoryItems(String playerName);

}
