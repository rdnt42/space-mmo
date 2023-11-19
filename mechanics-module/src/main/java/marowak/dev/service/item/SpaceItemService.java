package marowak.dev.service.item;

import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Item;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceItemService {

    Mono<Void> tryDropItemToSpace(Item item, Point coords);

    Mono<Void> initItem(ItemMessage message);

    Flux<ItemInSpaceView> getItemsInRange(Point coords);
}