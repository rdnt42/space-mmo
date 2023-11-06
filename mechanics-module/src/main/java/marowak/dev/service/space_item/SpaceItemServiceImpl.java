package marowak.dev.service.space_item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.Item;
import marowak.dev.service.broker.ItemClient;
import marowak.dev.service.physic.Utils;
import marowak.dev.service.probability.ProbabilityCalculationService;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static marowak.dev.enums.StorageType.STORAGE_TYPE_SPACE;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class SpaceItemServiceImpl implements SpaceItemService {
    private final ProbabilityCalculationService probabilityCalculationService;
    private final ItemClient itemClient;

    private final Map<Long, ItemInSpaceView> items = new ConcurrentHashMap<>();

    @Override
    public Flux<ItemInSpaceView> getItemsInRange(Point coords) {
        return Flux.fromStream(items.values().stream()
                .filter(item -> Utils.isInRange(coords, item.coords())));
    }

    @Override
    public Mono<Void> tryDropItemToSpace(Item item, Point coords) {
        return probabilityCalculationService.isItemDropped(item.getTypeId())
                .flatMap(isDropped -> {
                    if (Boolean.TRUE.equals(isDropped)) {
                        return addItemToSpace(item, coords)
                                .doOnNext(spaceItem -> log.info("Item dropped to space, id: {}", spaceItem.id()))
                                .flatMap(spaceItem -> {
                                    item.updateStorage(0, STORAGE_TYPE_SPACE.getStorageId());
                                    return sendItemUpdate(item.getView());
                                });
                    } else {
                        return sendItemDelete(item.getId())
                                .doOnNext(itemId -> log.info("Item deleted, id: {}", itemId));
                    }
                }).then();
    }

    private Mono<ItemInSpaceView> addItemToSpace(Item item, Point coords) {
        var newX = coords.x() + getCoordInExplosionRadius(-120, 120);
        var newY = coords.x() + getCoordInExplosionRadius(-100, 100);
        ItemInSpaceView itemInSpaceView =
                new ItemInSpaceView(item.getId(), new Point(newX, newY), item.getTypeId(), item.getDsc());
        items.put(item.getId(), itemInSpaceView);

        return Mono.just(itemInSpaceView);
    }

    private double getCoordInExplosionRadius(int min, int max) {
        return Math.random() * (max + 1 - min) + min;
    }

    private Mono<Long> sendItemUpdate(ItemView item) {
        ItemMessage message = ItemMessage.builder()
                .key(ItemMessageKey.ITEMS_UPDATE)
                .id(item.getId())
                .slotId(item.getSlotId())
                .storageId(item.getStorageId())
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send Items init error, key{}, character: {}, error: {}",
                        message.getKey(), message.getCharacterName(), e.getMessage()))
                .then(Mono.just(item.getId()));
    }

    private Mono<Long> sendItemDelete(long itemId) {
        ItemMessage message = ItemMessage.builder()
                .key(ItemMessageKey.ITEM_DELETE)
                .id(itemId)
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send Item delete error, key{}, character: {}, error: {}",
                        message.getKey(), message.getCharacterName(), e.getMessage()))
                .then(Mono.just(itemId));
    }
}
