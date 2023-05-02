package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.broker.ItemClient;
import message.ItemMessage;


@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final ItemClient itemClient;
    private final CharacterInventoryService characterInventoryService;

    @Override
    public void sendGetItems(ItemMessageKey key, String characterName) {
        // TODO add other items type
        ItemMessage message = ItemMessage.builder()
                .key(key)
                .build();

        itemClient.sendGetItems(message)
                .doOnError(e -> log.error("Send Items init error, key{}, character: {}, error: {}", key, characterName, e.getMessage()))
                .doOnSuccess(c -> log.info("Send Items init, key: {}, character: {}", key, characterName))
                .subscribe();
    }

    @Override
    public void updateItem(ItemMessage message) {
        characterInventoryService.updateInventory(message);
    }
}
