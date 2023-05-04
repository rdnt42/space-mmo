package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Engine;
import marowak.dev.entity.Item;
import marowak.dev.repository.EngineR2Repository;
import marowak.dev.repository.ItemR2Repository;
import marowak.dev.service.TriFunction;
import message.EngineMessage;
import message.ItemMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final EngineR2Repository engineR2Repository;
    private final ItemR2Repository itemR2Repository;

    @Override
    public Flux<ItemMessage> getAllOnline() {
        return Flux.from(engineR2Repository.findAll())
                .flatMap(engine -> Flux.from(itemR2Repository.findById(engine.id()))
                        .map(item -> engineToMessage.apply(engine, item, ItemMessageKey.ITEMS_GET_ALL)));
    }

    @Override
    public Flux<ItemMessage> getForCharacter(String characterName) {
        return Flux.empty();
    }

    @Override
    public Mono<ItemMessage> updateItem(ItemMessage message) {
        itemR2Repository.update(message.getId(), message.getSlotId());

        return Mono.empty();
    }

    private final TriFunction<Engine, Item, ItemMessageKey, ItemMessage> engineToMessage =
            (engine, item, key) -> EngineMessage.builder()
                    .key(key)
                    .id(engine.id())
                    .slotId(item.slotId())
                    .characterName(item.characterName())
                    .itemTypeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .speed(engine.speed())
                    .jump(engine.jump())
                    .engineType(engine.engineTypeId())
                    .build();
}
