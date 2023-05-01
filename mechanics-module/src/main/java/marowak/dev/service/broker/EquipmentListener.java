package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.equipment.EquipmentCommandService;
import message.EquipmentMessage;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class EquipmentListener {
    private static final String TOPIC_NAME = "equipments-answer";

    private final EquipmentCommandService equipmentCommandService;

    @Topic(TOPIC_NAME)
    public void receive(Flux<EquipmentMessage> equipments) {
        equipments
                .doOnError(e -> log.error("Topic {} receive error: ", TOPIC_NAME, e))
                .doOnNext(message -> log.debug("Topic {} receive message: {}, key: {}", TOPIC_NAME, message.getCharacterName(), message.getKey()))
                .flatMap(equipmentCommandService::executeCommand)
                .subscribe();
    }
}
