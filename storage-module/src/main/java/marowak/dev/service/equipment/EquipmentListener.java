package marowak.dev.service.equipment;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.entity.Equipment;
import marowak.dev.enums.EquipmentMessageKey;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class EquipmentListener {
    private final EquipmentService equipmentService;

    // TODO need uniform style
    @Topic("equipments")
    @SendTo("equipments-answer")
    public Iterable<Equipment> receive(@KafkaKey EquipmentMessageKey key, String characterName) {
        log.info("Get init characters command: {}, character name: {}", key, characterName);
        return switch (key) {
            case EQUIPMENTS_GET_ONE -> null;
            case EQUIPMENTS_GET_ALL -> equipmentService.getAllOnline();
        };
    }
}
