package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import keys.EquipmentMessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.equipment.EquipmentService;
import message.EngineMessage;
import message.EquipmentMessage;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class EquipmentListener {
    private static final String TOPIC_NAME = "equipments-answer";

    private final EquipmentService equipmentService;

    @Topic(TOPIC_NAME)
    public void receive(@KafkaKey EquipmentMessageKey key, Flux<EquipmentMessage> equipments) {
        equipments.doOnNext(e -> {
                    if (e instanceof EngineMessage) {
                        log.info("value: {}", ((EngineMessage) e).getSpeed());

                    }
                })
                .subscribe();
    }
}
