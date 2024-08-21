package marowak.dev.service.item;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.repository.ItemR2Repository;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final ItemR2Repository itemR2Repository;
    private final ItemMessageService itemMessageService;


    @Override
    public Flux<ItemMessage> getOnline() {
        return itemMessageService.findAll();
    }

    @Override
    public Flux<ItemMessage> getForCharacter(String characterName) {
        return itemMessageService.findAllByCharacterName(characterName);
    }

    @Override
    public Flux<ItemMessage> getItemsInSpace() {
        return itemMessageService.findAllInSpace();
    }

    @Override
    public Mono<ItemMessage> updateItem(ItemMessage message) {
        itemR2Repository.update(message.getId(), message.getSlotId(), message.getStorageId(), message.getCharacterName(),
                message.getX(), message.getY());

        return Mono.empty();
    }


    @Override
    public Mono<ItemMessage> deleteItem(ItemMessage message) {
        return itemMessageService.deleteById(message.getId())
                .then(Mono.empty());
    }
}
