package marowak.dev.service.item;

import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Flux<ItemMessage> getAllOnline();

    Flux<ItemMessage> getForCharacter(String characterName);

    Mono<ItemMessage> updateItem(ItemMessage message);

    Mono<ItemMessage> deleteItem(ItemMessage message);
}
