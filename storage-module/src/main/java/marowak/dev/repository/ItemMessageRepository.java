package marowak.dev.repository;

import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.enums.ItemType;
import marowak.dev.service.mapper.ItemMapper;
import marowak.dev.storage.ItemTypeStorage;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SuppressWarnings({"unchecked", "rawtypes"})
@RequiredArgsConstructor
@Singleton
public class ItemMessageRepository {
    private final ItemR2Repository itemR2Repository;
    private final ItemTypeStorage<ItemMapper> mappers;
    private final ItemTypeStorage<ReactiveStreamsCrudRepository<?, Long>> repositories;

    public Flux<ItemMessage> findAllInSpace() {
        return Flux.from(itemR2Repository.findByStorageId(3))
                .flatMap(item -> {
                    ItemType type = ItemType.from(item.itemTypeId());
                    var repository = repositories.getService(type);
                    return Mono.from(repository.findById(item.id()))
                            .map(extension -> {
                                var mapper = mappers.getService(type);
                                return mapper.map(item, extension);
                            });

                });
    }
}
