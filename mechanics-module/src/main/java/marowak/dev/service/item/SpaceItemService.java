package marowak.dev.service.item;

import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceItemService {

    Mono<Void> tryDropItemToSpace(Item item, Point coords);

    Flux<ItemInSpaceView> getItemsInRange(Point coords);
}
