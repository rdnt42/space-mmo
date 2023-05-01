package marowak.dev.service.equipment;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.messaging.annotation.SendTo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import message.EquipmentMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener(producerClientId = "equipments-producer")
public class EquipmentListener {
    private static final String TOPIC_NAME = "equipments";

    private final EquipmentCommandService equipmentCommandService;

    @Topic(TOPIC_NAME)
    @SendTo("equipments-answer")
    public Publisher<EquipmentMessage> receive(Flux<EquipmentMessage> messages) {
        return messages
                .doOnError(e -> log.error("Topic {} receive error: {}", TOPIC_NAME, e))
                .doOnNext(equipment -> log.info("Topic {} receive key: {}, characterName: {}, equipment id: {}",
                        TOPIC_NAME, equipment.getKey(), equipment.getCharacterName(), equipment.getId()))
                .flatMap(equipmentCommandService::executeCommand);
    }
}
