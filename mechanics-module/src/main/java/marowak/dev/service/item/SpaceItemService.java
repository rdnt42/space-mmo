package marowak.dev.service.item;

import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.ItemDto;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceItemService {

    Mono<Void> tryDropItemToSpace(ItemDto item, Point coords);

    Flux<ItemInSpaceView> getItemsInRange(Point coords);

    Mono<Void> addItem(ItemMessage item);

    Mono<Void> removeItem(long id);
}
