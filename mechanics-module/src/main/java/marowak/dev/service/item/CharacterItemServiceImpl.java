package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.api.request.ItemUpdate;
import marowak.dev.api.response.InventoryView;
import marowak.dev.api.response.item.ItemView;
import marowak.dev.character.Engine;
import marowak.dev.character.Item;
import marowak.dev.dto.item.*;
import marowak.dev.service.broker.ItemClient;
import marowak.dev.service.character.CharacterShipService;
import message.ItemMessage;
import org.apache.kafka.clients.producer.RecordMetadata;
import reactor.core.publisher.Mono;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class CharacterItemServiceImpl implements CharacterItemService {
    private final ItemClient itemClient;
    private final CharacterShipService characterShipService;

    @Override
    public Mono<RecordMetadata> sendGetItems(ItemMessageKey key, String characterName) {
        ItemMessage message = ItemMessage.builder()
                .key(key)
                .characterName(characterName)
                .build();

        return itemClient.sendItems(message)
                .doOnError(e -> log.error("Send getting Items init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send getting Items init, key: {}, character: {}", key, characterName));
    }

    @Override
    public Mono<Void> addItem(ItemDto dto) {
        Item item;
        switch (dto) {
            case EngineDto engine -> item = BuilderHelper.dtoToEngine.apply(engine);
            case FuelTankDto fuelTank -> item = BuilderHelper.dtoToFuelTank.apply(fuelTank);
            case CargoHookDto cargoHook -> item = BuilderHelper.dtoToCargoHook.apply(cargoHook);
            case HullDto hull -> item = BuilderHelper.dtoToHull.apply(hull);
            case WeaponDto weapon -> item = BuilderHelper.dtoToWeapon.apply(weapon);
            default -> throw new IllegalStateException("Unknown Item type, key: " + dto.getTypeId());
        }

        return characterShipService.addItem(dto.getCharacterName(), item)
                .then();
    }

    @Override
    public Mono<ItemUpdate> updateItem(ItemUpdate request, String characterName) {
        return characterShipService.updateItem(characterName, request)
                .flatMap(item -> sendItemUpdate(item.getView(), characterName)
                        .doOnNext(c -> log.info("Inventory updated from client id: {}, slot: {}", item.getId(), item.getSlotId()))
                        .then(Mono.just(ItemUpdate.builder()
                                .id(item.getId())
                                .slotId(item.getSlotId())
                                .storageId(item.getStorageId())
                                .build())));
    }

    @Override
    public Mono<ItemView> getItem(String characterName, long itemId) {
        return characterShipService.getItem(characterName, itemId);
    }

    @Override
    public Mono<InventoryView> getInventory(String characterName) {
        // TODO items

        return characterShipService.getInventory(characterName);
    }

    private Mono<Void> sendItemUpdate(ItemView item, String characterName) {
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

    private Item map(ItemDto itemDto) {
        switch (itemDto) {
            case EngineDto engine ->
        }
    }

    private Engine map(EngineDto engine) {
        return Engine.builder()
                .build();
    }

}
