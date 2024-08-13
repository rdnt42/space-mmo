package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.ItemUpdateRequest;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.character.CharacterShip;
import marowak.dev.character.Item;
import marowak.dev.dto.item.*;
import marowak.dev.enums.StorageType;
import marowak.dev.service.character.CharacterShipService;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterItemServiceImpl implements CharacterItemService {
    private final CharacterShipService characterShipService;
    private final ItemStorage itemStorage;

    @Override
    public Mono<Void> addItem(ItemMessage message) {
        return itemStorage.addItem(message)
                .flatMap(dto -> {
                    Item item = map(dto);
                    return characterShipService.addItem(dto.getCharacterName(), item)
                            .then();
                });
    }

    @Override
    public Mono<ItemView> addItemFromSpace(long itemId, String characterName) {
        return itemStorage.getItem(itemId)
                .flatMap(dto -> getFreeSlot(characterName)
                        .flatMap(slotId -> {
                            dto.setCharacterName(characterName);
                            dto.setStorageId(StorageType.STORAGE_TYPE_HOLD.getStorageId());
                            dto.setSlotId(slotId);

                            Item item = map(dto);
                            return characterShipService.addItem(characterName, item)
                                    .then(itemStorage.updateItem(dto))
                                    .doOnSuccess(i -> log.info("Character: {} got item: {} from space", i.getCharacterName(), i.getId()))
                                    .then(Mono.just(dto.getView()));
                        }));
    }

    @Override
    public Mono<ItemUpdateRequest> updateItem(ItemUpdateRequest request, String characterName) {
        return itemStorage.getItem(request.id())
                .flatMap(dto -> {
                    dto.setSlotId(request.slotId());
                    dto.setStorageId(request.storageId());
                    Item item = map(dto);

                    return characterShipService.updateItem(characterName, item)
                            .then(itemStorage.updateItem(dto))
                            .then(Mono.just(new ItemUpdateRequest(dto.getId(), dto.getSlotId(), dto.getStorageId())))
                            .doOnSuccess(u -> log.info("Inventory updated from client id: {}, slot: {}", u.id(), u.slotId()));
                });
    }

    @Override
    public Mono<InventoryView> getInventory(String characterName) {
        return characterShipService.getShip(characterName)
                .flatMap(ship -> {
                    int config = ship.getHull().getConfig();
                    Flux<ItemView> itemViewFlux = Flux.fromIterable(ship.getItems())
                            .flatMap(i -> itemStorage.getItem(i.getId())
                                    .map(ItemDto::getView));

                    return itemViewFlux.collectList()
                            .map(views -> new InventoryView(views, config));
                });
    }

    private Item map(ItemDto dto) {
        Item item;
        if (StorageType.STORAGE_TYPE_HULL.equals(dto.getStorageId())) {
            switch (dto) {
                case EngineDto engine -> item = BuilderHelper.dtoToEngine.apply(engine);
                case FuelTankDto fuelTank -> item = BuilderHelper.dtoToFuelTank.apply(fuelTank);
                case CargoHookDto cargoHook -> item = BuilderHelper.dtoToCargoHook.apply(cargoHook);
                case HullDto hull -> item = BuilderHelper.dtoToHull.apply(hull);
                case WeaponDto weapon -> item = BuilderHelper.dtoToWeapon.apply(weapon);
                default -> throw new IllegalStateException("Unknown Item type, key: " + dto.getTypeId());
            }
        } else if (StorageType.STORAGE_TYPE_HOLD.equals(dto.getStorageId())) {
            item = BuilderHelper.dtoToCargoItem.apply(dto);
        } else {
            throw new IllegalStateException("Unsupported storage id: " + dto.getStorageId());
        }

        return item;
    }

    private Mono<Integer> getFreeSlot(String characterName) {
        return characterShipService.getShip(characterName)
                .map(CharacterShip::getFreeSlot);
    }

}
