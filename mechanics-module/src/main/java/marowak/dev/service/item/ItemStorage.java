package marowak.dev.service.item;

import marowak.dev.api.request.ItemUpdate;
import marowak.dev.dto.item.ItemDto;
import message.ItemMessage;
import reactor.core.publisher.Mono;

public interface ItemStorage {
    Mono<ItemDto> addItem(ItemMessage message);

    Mono<ItemDto> getItem(long id);

    Mono<ItemDto> updateItem(ItemUpdate update);

    Mono<Long> deleteItem(long id);
}
