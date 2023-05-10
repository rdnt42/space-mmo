package marowak.dev.service.item;

import jakarta.inject.Singleton;
import keys.ItemMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.repository.CargoHookR2Repository;
import marowak.dev.repository.EngineR2Repository;
import marowak.dev.repository.FuelTankR2Repository;
import marowak.dev.repository.ItemR2Repository;
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

    private final CargoHookR2Repository cargoHookR2Repository;

    @Override
    public Flux<ItemMessage> getAllOnline() {
        Flux<ItemMessage> engineFlux = Flux.from(engineR2Repository.findAll())
                .flatMap(engine -> Flux.from(itemR2Repository.findById(engine.id()))
                        .map(item -> BuilderHelper.engineToMessage.apply(engine, item, ItemMessageKey.ITEMS_GET_ALL)));

        Flux<ItemMessage> fuelTankFlux = Flux.from(fuelTankR2Repository.findAll())
                .flatMap(fuelTank -> Flux.from(itemR2Repository.findById(fuelTank.id()))
                        .map(item -> BuilderHelper.fuelTankToMessage.apply(fuelTank, item, ItemMessageKey.ITEMS_GET_ALL)));

        Flux<ItemMessage> cargoHookFlux = Flux.from(cargoHookR2Repository.findAll())
                .flatMap(cargoHook -> Flux.from(itemR2Repository.findById(cargoHook.id()))
                        .map(item -> BuilderHelper.cargoHookToMessage.apply(cargoHook, item, ItemMessageKey.ITEMS_GET_ALL)));

        return Flux.concat(engineFlux, fuelTankFlux, cargoHookFlux);
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
}
