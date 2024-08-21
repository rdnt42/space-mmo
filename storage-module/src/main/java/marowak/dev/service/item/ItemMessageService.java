package marowak.dev.service.item;

import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Item;
import marowak.dev.enums.ItemType;
import marowak.dev.repository.ItemR2Repository;
import marowak.dev.service.mapper.ItemMapper;
import marowak.dev.storage.ItemTypeStorage;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemMessageService {
    private final ItemR2Repository itemR2Repository;
    private final ItemTypeStorage<ItemMapper> mappers;
    private final ItemTypeStorage<ReactiveStreamsCrudRepository<?, Long>> repositories;

    public Flux<ItemMessage> findAllInSpace() {
        return Flux.from(itemR2Repository.findByStorageId(3))
                .flatMap(this::findAndMapExtension)
                .flatMap(item -> copyWithKey(item, ItemMessageKey.ITEMS_GET_IN_SPACE));
    }

    public Flux<ItemMessage> findAll() {
        return Flux.from(itemR2Repository.findAll())
                .flatMap(this::findAndMapExtension)
                .flatMap(item -> copyWithKey(item, ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS));
    }

    public Flux<ItemMessage> findAllByCharacterName(String characterName) {
        return Flux.from(itemR2Repository.findByCharacterName(characterName))
                .flatMap(this::findAndMapExtension)
                .flatMap(item -> copyWithKey(item, ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER));
    }

    public Mono<ItemMessage> deleteById(long id) {
        return Mono.from(itemR2Repository.findById(id))
                .doOnNext(i -> log.info("Try to delete item with id: {}", i.id()))
                .flatMap(item -> {
                            ItemType type = ItemType.from(item.itemTypeId());
                            var repository = repositories.getService(type);
                            return Mono.from(repository.deleteById(id));
                        }
                )
                .doOnError(e -> log.error("Error when deleting item with id: {}", id, e))
                .doOnSuccess(i -> log.info("Item deleted with id: {}", id))
                .map(itemId -> ItemMessage.builder()
                        .id(itemId)
                        .build());
    }

    private Mono<ItemMessage> findAndMapExtension(Item item) {
        ItemType type = ItemType.from(item.itemTypeId());
        var repository = repositories.getService(type);
        return Mono.from(repository.findById(item.id()))
                .map(extension -> {
                    var mapper = mappers.getService(type);
                    return mapper.map(item, extension);
                });
    }

    private Mono<ItemMessage> copyWithKey(ItemMessage message, ItemMessageKey key) {
        return Mono.just(message.copy()
                .key(key)
                .build());
    }
}
