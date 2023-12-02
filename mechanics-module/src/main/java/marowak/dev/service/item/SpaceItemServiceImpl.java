package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.response.item.ItemInSpaceView;
import marowak.dev.dto.Point;
import marowak.dev.dto.item.HullDto;
import marowak.dev.dto.item.ItemDto;
import marowak.dev.service.physic.Utils;
import marowak.dev.service.probability.ProbabilityCalculationService;
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
    private final ItemStorage itemStorage;
    private final Map<Long, ItemInSpaceView> items = new ConcurrentHashMap<>();

    @Override
    public Flux<ItemInSpaceView> getItemsInRange(Point coords) {
        return Flux.fromStream(items.values().stream()
                .filter(item -> Utils.isInRange(coords, item.coords())));
    }

    @Override
    public Mono<Void> addItem(ItemDto item) {
        var spaceItem = ItemInSpaceView.builder()
                .id(item.getId())
                .x(item.getX())
                .y(item.getY())
                .itemTypeId(item.getTypeId())
                .name(item.getName())
                .dsc(item.getDsc())
                .build();
        items.put(item.getId(), spaceItem);
        log.info("Added item to space, id: {}, x: {}, y:{}", spaceItem.id(), spaceItem.x(), spaceItem.y());

        return Mono.empty();
    }

    @Override
    public Mono<Void> tryDropItemToSpace(long itemId, Point coords) {
        return itemStorage.getItem(itemId)
                .flatMap(item -> {
                    if (item instanceof HullDto) {
                        return itemStorage.deleteItem(itemId)
                                .doOnSuccess(delId -> log.info("Item deleted, id: {}", delId))
                                .then();
                    }

                    return probabilityCalculationService.isItemDropped(item.getTypeId())
                            .flatMap(isDropped -> {
                                if (Boolean.TRUE.equals(isDropped)) {
                                    return addItemToSpace(item, coords)
                                            .flatMap(spaceItem -> {
                                                item.setStorageId(STORAGE_TYPE_SPACE.getStorageId());
                                                item.setSlotId(0);
                                                item.setCharacterName(null);
                                                item.setX(spaceItem.x());
                                                item.setY(spaceItem.y());
                                                return itemStorage.updateItem(item);
                                            })
                                            .doOnNext(id -> log.info("Item dropped to space, id: {}", id));
                                } else {
                                    return itemStorage.deleteItem(item.getId())
                                            .doOnNext(delId -> log.info("Item deleted, id: {}", delId));
                                }
                            }).then();
                });
    }

    @Override
    public Mono<Long> removeItem(long itemId) {
        ItemInSpaceView removed = items.remove(itemId);

        return Mono.just(removed.id());
    }

    private Mono<ItemInSpaceView> addItemToSpace(ItemDto item, Point coords) {
        var newX = coords.x() + getCoordInExplosionRadius(-120, 120);
        var newY = coords.x() + getCoordInExplosionRadius(-100, 100);
        var itemInSpace = ItemInSpaceView.builder()
                .id(item.getId())
                .x(newX)
                .y(newY)
                .itemTypeId(item.getTypeId())
                .name(item.getName())
                .dsc(item.getDsc())
                .build();
        items.put(item.getId(), itemInSpace);

        return Mono.just(itemInSpace);
    }

    private double getCoordInExplosionRadius(int min, int max) {
        return Math.random() * (max + 1 - min) + min;
    }

}
