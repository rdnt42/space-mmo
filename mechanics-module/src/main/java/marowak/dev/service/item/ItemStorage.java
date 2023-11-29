package marowak.dev.service.item;

import keys.ItemMessageKey;
import marowak.dev.dto.item.ItemDto;
import message.ItemMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

public interface ItemStorage {
    Mono<ItemDto> addItem(ItemMessage message);

    Mono<ItemDto> getItem(long id);

    Mono<ItemDto> updateItem(ItemDto update);

    Mono<Long> deleteItem(long id);

    Mono<RecordMetadata> sendGetItem(ItemMessageKey key, String characterName);
}
