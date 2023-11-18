package marowak.dev.service.item;

import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Flux<ItemMessage> getOnline();

    Flux<ItemMessage> getForCharacter(String characterName);

    Flux<ItemMessage> getItemsInSpace();

    Mono<ItemMessage> updateItem(ItemMessage message);

    Mono<ItemMessage> updateSpaceItem(ItemMessage message);

    Mono<ItemMessage> deleteItem(ItemMessage message);
}
