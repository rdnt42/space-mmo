package marowak.dev.service.equipment;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import message.EquipmentMessage;
import org.reactivestreams.Publisher;

@RequiredArgsConstructor
@Singleton
public class EquipmentCommandService {
    private final EquipmentService equipmentService;

    public Publisher<EquipmentMessage> executeCommand(EquipmentMessage message) {
        return switch (message.getKey()) {
            case EQUIPMENTS_GET_ONE -> equipmentService.getForCharacter(message.getCharacterName());
            case EQUIPMENTS_GET_ALL -> equipmentService.getAllOnline();
        };
    }

}
