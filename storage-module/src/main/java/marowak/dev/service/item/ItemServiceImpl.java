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


    // TODO generic
    @Override
    public Flux<ItemMessage> getAllOnline() {
        Flux<ItemMessage> engineFlux = Flux.from(engineR2Repository.findAll())
                .flatMap(engine -> Flux.from(itemR2Repository.findById(engine.id()))
                        .map(item -> BuilderHelper.engineToMessage.apply(engine, item, ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS)));

        Flux<ItemMessage> fuelTankFlux = Flux.from(fuelTankR2Repository.findAll())
                .flatMap(fuelTank -> Flux.from(itemR2Repository.findById(fuelTank.id()))
                        .map(item -> BuilderHelper.fuelTankToMessage.apply(fuelTank, item, ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS)));

        Flux<ItemMessage> cargoHookFlux = Flux.from(cargoHookR2Repository.findAll())
                .flatMap(cargoHook -> Flux.from(itemR2Repository.findById(cargoHook.id()))
                        .map(item -> BuilderHelper.cargoHookToMessage.apply(cargoHook, item, ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS)));

        Flux<ItemMessage> hullFlux = Flux.from(hullR2Repository.findAll())
                .flatMap(hull -> Flux.from(itemR2Repository.findById(hull.id()))
                        .map(item -> BuilderHelper.hullToMessage.apply(hull, item, ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS)));

        Flux<ItemMessage> weaponFlux = Flux.from(weaponR2Repository.findAll())
                .flatMap(weapon -> Flux.from(itemR2Repository.findById(weapon.id()))
                        .map(item -> BuilderHelper.weaponToMessage.apply(weapon, item, ItemMessageKey.ITEMS_GET_FOR_ALL_CHARACTERS)));

        return Flux.concat(engineFlux, fuelTankFlux, cargoHookFlux, hullFlux, weaponFlux);
    }

    @Override
    public Flux<ItemMessage> getForCharacter(String characterName) {
        return itemR2Repository.findByCharacterName(characterName)
                .flatMap(item -> switch (item.itemTypeId()) {
                    case 1 -> Flux.from(engineR2Repository.findById(item.id()))
                            .map(engine -> BuilderHelper.engineToMessage.apply(engine, item, ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER));
                    case 2 -> Flux.from(fuelTankR2Repository.findById(item.id()))
                            .map(fuel -> BuilderHelper.fuelTankToMessage.apply(fuel, item, ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER));
                    case 6 -> Flux.from(cargoHookR2Repository.findById(item.id()))
                            .map(hook -> BuilderHelper.cargoHookToMessage.apply(hook, item, ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER));
                    case 8 -> Flux.from(hullR2Repository.findById(item.id()))
                            .map(hull -> BuilderHelper.hullToMessage.apply(hull, item, ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER));
                    case 9, 10, 11, 12, 13 -> Flux.from(weaponR2Repository.findById(item.id()))
                            .map(hull -> BuilderHelper.weaponToMessage.apply(hull, item, ItemMessageKey.ITEMS_GET_FOR_ONE_CHARACTER));

                    default -> Flux.error(new IllegalStateException("Unexpected value: " + item.itemTypeId()));
                });
    }

    @Override
    public Flux<ItemMessage> getItemsInSpace() {
        return Flux.empty();
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
