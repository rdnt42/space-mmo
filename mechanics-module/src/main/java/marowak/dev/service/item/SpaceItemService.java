package marowak.dev.service.item;

import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.dto.Point;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceItemService {

    Flux<ItemInSpaceView> getItemsInRange(Point coords);

    Mono<Void> addItem(ItemMessage item);

    Mono<Void> tryDropItemToSpace(long itemId, Point coords);

    Mono<Long> removeItem(long itemId);
}
