package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.dto.item.ItemDto;
import marowak.dev.service.broker.ItemClient;
import message.*;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, ItemDto> itemsMap = new ConcurrentHashMap<>();
    private final ItemClient itemClient;

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
    public Mono<ItemDto> updateItem(ItemDto update) {
        ItemDto dto = Optional.ofNullable(itemsMap.get(update.getId()))
                .orElseThrow();

        return sendItemUpdate(dto, dto.getCharacterName())
                .then(Mono.just(dto));
    }

    @Override
    public Mono<Long> deleteItem(long id) {
        ItemDto removed = itemsMap.remove(id);
        return sendDeleteItem(removed.getId());
    }

    @Override
    public Mono<RecordMetadata> sendGetItem(ItemMessageKey key, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(key)
                .characterName(characterName)
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send getting Items init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send getting Items init, key: {}, character: {}", key, characterName));
    }

    private Mono<Void> sendItemUpdate(ItemDto item, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(ItemMessageKey.ITEM_UPDATE)
                .id(item.getId())
                .characterName(characterName)
                .slotId(item.getSlotId())
                .storageId(item.getStorageId())
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send Items init error, key{}, character: {}, error: {}",
                        message.getKey(), message.getCharacterName(), e.getMessage()))
                .then();
    }

    private Mono<Long> sendDeleteItem(long itemId) {
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
