package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.dto.item.ItemDto;
import marowak.dev.enums.StorageType;
import message.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Singleton
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, ItemDto> itemsMap = new ConcurrentHashMap<>();
    private final CharacterItemService characterItemService;

    @Override
    public Mono<ItemDto> addItem(ItemMessage message) {
        ItemDto item;
        switch (message) {
            case EngineMessage engine -> item = BuilderHelper.engineMessageToItem.apply(engine);
            case FuelTankMessage fuelTank -> item = BuilderHelper.fuelTankMessageToItem.apply(fuelTank);
            case CargoHookMessage cargoHook -> item = BuilderHelper.cargoHookMessageToItem.apply(cargoHook);
            case HullMessage hull -> item = BuilderHelper.hullMessageToItem.apply(hull);
            case WeaponMessage weapon -> item = BuilderHelper.weaponMessageToItem.apply(weapon);
            default -> throw new IllegalStateException("Unknown Item message, key: " + message.getKey());
        }
        // TODO
        StorageType type = StorageType.from(item.getStorageId());
        if (type.isSpaceStorage()) {

        } else if (type.isShipStorage()) {
            characterItemService.addItem(item)
                    .subscribe();
        }
        itemsMap.put(item.getId(), item);

        return Mono.just(item);
    }

    @Override
    public Mono<ItemDto> getItem(long id) {
        ItemDto dto = Optional.ofNullable(itemsMap.get(id))
                .orElseThrow();

        return Mono.just(dto);
    }

    @Override
    public Mono<ItemDto> updateItem(ItemUpdate update) {
        return null;
    }

    @Override
    public Mono<Long> deleteItem(long id) {
        return null;
    }
}
