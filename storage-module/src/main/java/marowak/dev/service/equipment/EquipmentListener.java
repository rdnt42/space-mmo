package marowak.dev.service.equipment;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import keys.EquipmentMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.EquipmentMessage;
import org.reactivestreams.Publisher;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(producerClientId = "equipments-producer")
public class EquipmentListener {
    private static final String TOPIC_NAME = "characters";
    private final EquipmentService equipmentService;

    @Topic(TOPIC_NAME)
    @SendTo("equipments-answer")
    public Publisher<EquipmentMessage> receive(@KafkaKey EquipmentMessageKey key, String characterName) {
        log.info("Get equipments command: {}, character name: {}", key, characterName);
        return switch (key) {
            case EQUIPMENTS_GET_ONE -> null;
            case EQUIPMENTS_GET_ALL -> equipmentService.getAllOnline();
        };
    }
}
