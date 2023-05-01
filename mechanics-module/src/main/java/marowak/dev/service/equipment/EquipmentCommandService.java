package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import message.EquipmentMessage;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Singleton
public class EquipmentCommandService {
    private final EquipmentService equipmentService;

    public Publisher<Void> executeCommand(EquipmentMessage message) {
        switch (message.getKey()) {
            case EQUIPMENTS_GET_ALL, EQUIPMENTS_GET_ONE -> equipmentService.updateEquipment(message);
            default -> { // Ignore other events
            }
        }

        return Mono.empty();
    }
}
