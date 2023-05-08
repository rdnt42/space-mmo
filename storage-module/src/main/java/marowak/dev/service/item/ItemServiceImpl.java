package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Engine;
import marowak.dev.entity.FuelTank;
import marowak.dev.entity.Item;
import marowak.dev.repository.EngineR2Repository;
import marowak.dev.repository.FuelTankR2Repository;
import marowak.dev.repository.ItemR2Repository;
import marowak.dev.service.TriFunction;
import message.EngineMessage;
import message.FuelTankMessage;
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

    @Override
    public Flux<ItemMessage> getAllOnline() {
        Flux<ItemMessage> engineFlux = Flux.from(engineR2Repository.findAll())
                .flatMap(engine -> Flux.from(itemR2Repository.findById(engine.id()))
                        .map(item -> engineToMessage.apply(engine, item, ItemMessageKey.ITEMS_GET_ALL)));

        Flux<ItemMessage> fuelTankFlux = Flux.from(fuelTankR2Repository.findAll())
                .flatMap(fuelTank -> Flux.from(itemR2Repository.findById(fuelTank.id()))
                        .map(item -> fuelTankToMessage.apply(fuelTank, item, ItemMessageKey.ITEMS_GET_ALL)));

        return Flux.concat(engineFlux, fuelTankFlux);
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
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .speed(engine.speed())
                    .jump(engine.jump())
                    .equipmentTypeId(engine.engineTypeId())
                    .build();

    private final TriFunction<FuelTank, Item, ItemMessageKey, ItemMessage> fuelTankToMessage =
            (fuelTank, item, key) -> FuelTankMessage.builder()
                    .key(key)
                    .id(fuelTank.id())
                    .slotId(item.slotId())
                    .characterName(item.characterName())
                    .typeId(item.itemTypeId())
                    .upgradeLevel(item.upgradeLevel())
                    .cost(item.cost())
                    .name(item.nameRu())
                    .dsc(item.dscRu())
                    .capacity(fuelTank.capacity())
                    .equipmentTypeId(fuelTank.fuelTankTypeId())
                    .build();
}
