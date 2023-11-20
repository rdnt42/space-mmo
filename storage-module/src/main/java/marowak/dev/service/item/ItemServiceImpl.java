package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.repository.ItemMessageRepository;
import marowak.dev.repository.ItemR2Repository;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final ItemR2Repository itemR2Repository;
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
    public Mono<ItemMessage> updateSpaceItem(ItemMessage message) {
        itemR2Repository.updateSpaceItem(message.getId(), message.getX(), message.getY());

        return Mono.empty();
    }

    @Override
    public Mono<ItemMessage> deleteItem(ItemMessage message) {
        return itemMessageRepository.deleteById(message.getId())
                .then(Mono.empty());
    }
}
