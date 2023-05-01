package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import keys.EquipmentMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Engine;
import marowak.dev.entity.Equipment;
import marowak.dev.repository.EngineR2Repository;
import marowak.dev.repository.EquipmentR2Repository;
import message.EngineMessage;
import message.EquipmentMessage;
import reactor.core.publisher.Flux;

import java.util.function.BiFunction;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EngineR2Repository engineR2Repository;
    private final EquipmentR2Repository equipmentR2Repository;

    @Override
    public Flux<EquipmentMessage> getAllOnline() {
        Flux<Engine> engines = Flux.from(engineR2Repository.findAll());
        Flux<Equipment> equipments = engines.flatMap(engine -> equipmentR2Repository.findById(engine.id()));

        return Flux.zip(engines, equipments).map(tuple -> engineToMessage.apply(tuple.getT1(), tuple.getT2()))
                .map(message -> messageToMessage.apply((EngineMessage) message, EquipmentMessageKey.EQUIPMENTS_GET_ALL));
    }

    @Override
    public Flux<EquipmentMessage> getForCharacter(String characterName) {
        return Flux.empty();
    }

    // TODO fix this
    private final BiFunction<EngineMessage, EquipmentMessageKey, EquipmentMessage> messageToMessage =
            (message, key) -> EngineMessage.builder()
                    .key(key)
                    .id(message.getId())
                    .slotId(message.getSlotId())
                    .equipped(message.isEquipped())
                    .characterName(message.getCharacterName())
                    .equipmentTypeId(message.getEquipmentTypeId())
                    .speed(message.getSpeed())
                    .upgradeLevel(message.getUpgradeLevel())
                    .cost(message.getCost())
                    .build();

    private final BiFunction<Engine, Equipment, EquipmentMessage> engineToMessage =
            (engine, equipment) -> EngineMessage.builder()
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
