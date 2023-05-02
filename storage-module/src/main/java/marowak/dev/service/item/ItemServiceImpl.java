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

@Slf4j
@RequiredArgsConstructor
@Singleton
public class ItemServiceImpl implements ItemService {
    private final EngineR2Repository engineR2Repository;
    private final ItemR2Repository itemR2Repository;

    @Override
    public Flux<ItemMessage> getAllOnline() {
        Flux<Engine> engines = Flux.from(engineR2Repository.findAll());
        Flux<Item> items = engines
                .flatMap(engine -> itemR2Repository.findById(engine.id()));

        return Flux.zip(engines, items)
                .map(tuple -> engineToMessage.apply(tuple.getT1(), tuple.getT2(), ItemMessageKey.ITEMS_GET_ALL));
    }

    @Override
    public Flux<ItemMessage> getForCharacter(String characterName) {
        return Flux.empty();
    }

    private final TriFunction<Engine, Item, ItemMessageKey, ItemMessage> engineToMessage =
            (engine, item, key) -> EngineMessage.builder()
                    .key(key)
                    .id(engine.id())
                    .slotId(item.slotId())
                    .equipped(item.equipped())
                    .characterName(item.characterName())
                    .itemTypeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .speed(engine.speed())
                    .jump(engine.jump())
                    .build();
}
