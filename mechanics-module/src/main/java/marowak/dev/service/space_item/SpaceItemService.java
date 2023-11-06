package marowak.dev.service.space_item;

import marowak.dev.dto.Point;
import marowak.dev.dto.item.Item;
import marowak.dev.dto.item.ItemInSpace;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceItemService {

    Mono<Void> tryDropItemToSpace(Item item, Point coords);

    Flux<ItemInSpace> getItemsInRange(Point coords);
}
