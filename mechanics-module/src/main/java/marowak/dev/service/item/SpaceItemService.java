package marowak.dev.service.item;

import marowak.dev.api.response.item.ItemInSpace;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Item;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SpaceItemService {

    Mono<Void> tryDropItemToSpace(Item item, Point coords);

    Flux<ItemInSpace> getItemsInRange(Point coords);

    Mono<Void> addItem(ItemMessage item);
}
