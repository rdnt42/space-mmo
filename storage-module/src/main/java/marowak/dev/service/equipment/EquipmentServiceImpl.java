package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import keys.EquipmentMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Engine;
import marowak.dev.entity.Equipment;
import marowak.dev.repository.EngineR2Repository;
import marowak.dev.repository.EquipmentR2Repository;
import marowak.dev.service.TriFunction;
import message.EngineMessage;
import message.EquipmentMessage;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EngineR2Repository engineR2Repository;
    private final EquipmentR2Repository equipmentR2Repository;

    @Override
    public Flux<EquipmentMessage> getAllOnline() {
        Flux<Engine> engines = Flux.from(engineR2Repository.findAll());
        Flux<Equipment> equipments = engines
                .flatMap(engine -> equipmentR2Repository.findById(engine.id()));

        return Flux.zip(engines, equipments)
                .map(tuple -> engineToMessage.apply(tuple.getT1(), tuple.getT2(), EquipmentMessageKey.EQUIPMENTS_GET_ALL));
    }

    @Override
    public Flux<EquipmentMessage> getForCharacter(String characterName) {
        return Flux.empty();
    }

    private final TriFunction<Engine, Equipment, EquipmentMessageKey, EquipmentMessage> engineToMessage =
            (engine, equipment, key) -> EngineMessage.builder()
                    .key(key)
                    .id(engine.id())
                    .slotId(equipment.slotId())
                    .equipped(equipment.equipped())
                    .characterName(equipment.characterName())
                    .equipmentTypeId(equipment.equipmentTypeId())
                    .upgradeLevel(equipment.upgradeLevel())
                    .speed(engine.speed())
                    .cost(engine.cost())
                    .build();
}
