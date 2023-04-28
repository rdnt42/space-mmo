package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marowak.dev.service.equipment.EquipmentService;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@KafkaListener
public class EquipmentListener {
    private final EquipmentService equipmentService;

    @Topic("equipments-answer")
    public void receive(@KafkaKey String key, Flux<String> equipments) {
        equipments.doOnNext(e -> log.info("value: {}", e))
                .subscribe();
    }
}
