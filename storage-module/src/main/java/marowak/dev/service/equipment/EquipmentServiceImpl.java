package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import keys.EquipmentMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Engine;
import marowak.dev.repository.EngineR2Repository;
import message.EngineMessage;
import message.EquipmentMessage;
import reactor.core.publisher.Flux;

import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EngineR2Repository engineR2Repository;

    @Override
    public Flux<EquipmentMessage> getAllOnline() {
        return Flux.from(engineR2Repository.findAll())
                .map(engine -> engineToMessage.apply(engine, EquipmentMessageKey.EQUIPMENTS_GET_ALL));
    }

    private final BiFunction<Engine, EquipmentMessageKey, EquipmentMessage> engineToMessage =
            (engine, key) -> EngineMessage.builder()
                    .key(key)
                    .id(engine.id())
                    .slotId(engine.slotId())
                    .equipped(engine.equipped())
                    .characterName(engine.characterName())
                    .engineTypeId(engine.engineTypeId())
                    .speed(engine.speed())
                    .upgradeLevel(engine.upgradeLevel())
                    .cost(engine.cost())
                    .build();
}
