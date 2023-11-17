package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.repository.*;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final EngineR2Repository engineR2Repository;
    private final FuelTankR2Repository fuelTankR2Repository;
    private final ItemR2Repository itemR2Repository;
    private final HullR2Repository hullR2Repository;
    private final WeaponR2Repository weaponR2Repository;
    private final CargoHookR2Repository cargoHookR2Repository;
    private final ItemMessageRepository itemMessageRepository;


    @Override
    public Flux<ItemMessage> getOnline() {
        return itemMessageRepository.findAll()
                .map(message -> {
                    message.setKey(ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS);
                    return message;
                });
    }

    @Override
    public Flux<ItemMessage> getForCharacter(String characterName) {
        return itemMessageRepository.findAllByCharacterName(characterName)
                .map(message -> {
                    message.setKey(ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER);
                    return message;
                });
    }

    @Override
    public Flux<ItemMessage> getItemsInSpace() {
        return itemMessageRepository.findAllInSpace()
                .map(message -> {
                    message.setKey(ItemMessageKey.ITEMS_GET_IN_SPACE);
                    return message;
                });
    }

    @Override
    public Mono<ItemMessage> updateItem(ItemMessage message) {
        itemR2Repository.update(message.getId(), message.getSlotId(), message.getStorageId(), message.getCharacterName());

        return Mono.empty();
    }

    @Override
    public Mono<ItemMessage> deleteItem(ItemMessage message) {
        Long id = message.getId();
        return Mono.from(itemR2Repository.findById(id))
                .doOnNext(i -> log.info("Try to delete item with id: {}", i.id()))
                .flatMap(item -> Mono.from(
                                switch (item.itemTypeId()) {
                                    case 1 -> Mono.from(engineR2Repository.deleteById(item.id()));
                                    case 2 -> Mono.from(fuelTankR2Repository.deleteById(item.id()));
                                    case 6 -> Mono.from(cargoHookR2Repository.deleteById(item.id()));
                                    case 8 -> Mono.from(hullR2Repository.deleteById(item.id()));
                                    case 9 -> Mono.from(weaponR2Repository.deleteById(item.id()));

                                    default ->
                                            Mono.error(new IllegalStateException("Unexpected value: " + item.itemTypeId()));
                                })

                        .flatMap(itemId -> Mono.from(itemR2Repository.deleteById(id)))
                )
                .doOnError(e -> log.error("Error when deleting item with id: {}", id, e))
                .doOnSuccess(i -> log.info("Item deleted with id: {}", id))
                .map(itemId -> ItemMessage.builder()
                        .key(ItemMessageKey.ITEM_DELETE)
                        .id(itemId)
                        .build());
    }
}
