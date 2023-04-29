package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Engine;
import marowak.dev.repository.EngineR2Repository;
import message.EngineMessage;
import message.EquipmentMessage;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Singleton
public class EquipmentServiceImpl implements EquipmentService {
    private final EngineR2Repository engineR2Repository;

    @Override
    public Flux<EquipmentMessage> getAllOnline() {
        return Flux.from(engineR2Repository.findAll())
                .map(engineToMessage);
    }

    @Override
    public Flux<EquipmentMessage> getForCharacter(String characterName) {
        return engineR2Repository.findByCharacterName(characterName)
                .map(engineToMessage);
    }

    Function<Engine, EquipmentMessage> engineToMessage = engine -> EngineMessage.builder()
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
