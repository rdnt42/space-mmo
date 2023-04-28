package marowak.dev.service.broker;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import lombok.RequiredArgsConstructor;
import marowak.dev.service.equipment.EquipmentService;

@RequiredArgsConstructor
@KafkaListener
public class EquipmentListener {
    private final EquipmentService equipmentService;

//    @Topic("equipments-answer")
//    public void receive(Flux<>) {
//
//    }
}
